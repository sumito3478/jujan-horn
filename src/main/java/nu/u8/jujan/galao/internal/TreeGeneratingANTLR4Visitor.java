// Copyright (C) 2015 Tomoaki Takezoe (a.k.a @sumito3478) <sumito3478@gmail.com>
//
// This software is free software; you can redistribute it and/or modify it
// under the terms of the GNU Lesser General Public License as published by the
// Free Software Foundation; either version 3 of the License, or (at your
// option) any later version.
//
// This software is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
// for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this software. If not, see http://www.gnu.org/licenses/.

package nu.u8.jujan.galao.internal;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Value(staticConstructor = "of")
@EqualsAndHashCode(callSuper = true)
public class TreeGeneratingANTLR4Visitor extends AbstractParseTreeVisitor<Tree>
    implements JujanVisitor<Tree> {
  private final String fileName;
  private final Tree.Factory factory;

  private Tree.Location location(ParserRuleContext ctx) {
    return Tree.Location.of(fileName, ctx.getStart().getLine(),
        ctx.getStart().getCharPositionInLine());
  }
  private Tree.Location location(Token token) {
    return Tree.Location.of(fileName, token.getLine(), token.getCharPositionInLine());
  }

  @Override
  public Tree.Expression visitLiteral(JujanParser.LiteralContext ctx) {
    if (ctx == null)
      return null;
    val l = location(ctx);
    if (ctx.TextLiteral() != null) {
      val text = ctx.TextLiteral().getText();
      return factory.newTextLiteral(l, text.substring(1, text.length() - 1));
    }
    if (ctx.NilLiteral() != null)
      return factory.newNilLiteral(l);
    if (ctx.BooleanLiteral() != null)
      return factory.newBooleanLiteral(l, Boolean.valueOf(ctx.BooleanLiteral().getText()));
    if (ctx.Int32Literal() != null) {
      val text = ctx.Int32Literal().getText();
      return factory.newInt32Literal(l, Integer.valueOf(text.substring(0, text.length() - "i32"
          .length())));
    }
    if (ctx.BigIntLiteral() != null)
      return factory.newBigIntLiteral(l, new BigInteger(ctx.BigIntLiteral().getText()));
    throw new InternalError(String.format("Unknown literal \"%s\"", ctx.getText()));
  }
  @Override
  public Tree.Identifier visitIdentifier(JujanParser.IdentifierContext ctx) {
    if (ctx == null)
      return null;
    val text = ctx.getText();
    return factory.newIdentifier(location(ctx), text.startsWith("'") ? text.substring(1, text
        .length() - 1) : text);
  }
  @Override
  public Tree.QualifiedIdentifier visitQualifiedIdentifier(JujanParser.QualifiedIdentifierContext ctx) {
    if (ctx == null)
      return null;
    val builder = new ImmutableList.Builder<Tree.Identifier>();
    builder.add(visitIdentifier(ctx.first));
    builder.addAll(ctx.identifier().stream().map(this::visitIdentifier).collect(Collectors.toList
        ()));
    return factory.newQualifiedIdentifier(location(ctx), builder.build());
  }
  @Override
  public Tree.RecordTypeExpression visitRecordTypeExpression(JujanParser.RecordTypeExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newRecordTypeExpression(location(ctx),
        StreamUtil.zip(ctx.identifier().stream().map
                (this::visitIdentifier),
            ctx.typeExpression().stream().map(this::visitTypeExpression))
            .collect(Collectors.toList()));
  }
  @Override
  public Tree.LambdaTypeExpression visitLambdaTypeExpression(JujanParser.LambdaTypeExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newLambdaTypeExpression(location(ctx),
        StreamUtil.zip(ctx.identifier().stream().map(this::visitIdentifier),
            ctx.typeExpression().stream().map(this::visitTypeExpression)).collect(Collectors.toList()),
        visitTypeExpression(ctx.resultType));
  }
  @Override
  public Tree.ListTypeExpression visitListTypeExpression(JujanParser.ListTypeExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newListTypeExpression(location(ctx),
        ctx.typeExpression().stream().map(this::visitTypeExpression).collect(Collectors.toList()),
        ctx.lastStar != null);
  }
  @Override
  public Tree.TypeExpression visitTypeExpression(JujanParser.TypeExpressionContext ctx) {
    if (ctx == null)
      return null;
    val l = location(ctx);
    if (ctx.left != null) {
      val op = ctx.operator;
      return factory.newBinaryOperationTypeExpression(l, visitTypeExpression(ctx.left),
          factory.newIdentifier(location(op), op.getText()), visitTypeExpression(ctx.right));
    }
    if (ctx.typeofOperand != null) {
      return factory.newTypeOfTypeExpression(l, visitQualifiedIdentifier(ctx.typeofOperand));
    }
    if (ctx.qualifiedIdentifier() != null) {
      return factory.newQualifiedTypeIdentifier(l, visitQualifiedIdentifier(ctx
          .qualifiedIdentifier()).getComponents());
    }
    val ret = visitChildren(ctx);
    if (!(ret instanceof Tree.TypeExpression))
      throw new InternalError(String.format("Could not recognize type expression: %s", ctx
          .getText()));
    return (Tree.TypeExpression) ret;
  }
  @Override
  public Tree.TypeDeclaration visitTypeDeclaration(JujanParser.TypeDeclarationContext ctx) {
    if (ctx == null)
      return null;
    return factory.newTypeDeclaration(location(ctx),
        visitIdentifier(ctx.identifier()),
        factory.newIdentifier(location(ctx.operator), ctx.operator.getText()),
        visitTypeExpression(ctx.typeExpression()));
  }
  @Override
  public Tree.LambdaExpressionParameter visitLambdaExpressionParameter(
      JujanParser.LambdaExpressionParameterContext ctx) {
    if (ctx == null)
      return null;
    return factory.newLambdaExpressionParameter(
        location(ctx),
        visitIdentifier(ctx.identifier()),
        visitTypeExpression(ctx.typeExpression()),
        visitExpression(ctx.expression())
    );
  }
  @Override
  public Tree.LambdaExpression visitLambdaExpression(JujanParser.LambdaExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newLambdaExpression(location(ctx),
        ctx.lambdaExpressionParameter().stream().map(this::visitLambdaExpressionParameter)
            .collect(Collectors.toList()),
        visitTypeExpression(ctx.typeExpression()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.RecordExpression visitRecordExpression(JujanParser.RecordExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newRecordExpression(
        location(ctx),
        StreamUtil.zip(
            ctx.identifier().stream().map(this::visitIdentifier),
            ctx.expression().stream().map(this::visitExpression))
            .collect(Collectors.toList()));
  }
  @Override
  public Tree.ListExpression visitListExpression(JujanParser.ListExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newListExpression(
        location(ctx),
        ctx.expression().stream().map(this::visitExpression).collect(Collectors.toList()));
  }
  @Override
  public Tree.CompoundExpressionElement visitCompoundExpressionElement(JujanParser.CompoundExpressionElementContext ctx) {
    if (ctx == null)
      return null;
    if (ctx.declaration() != null)
      return visitDeclaration(ctx.declaration());
    if (ctx.typeDeclaration() != null)
      return visitTypeDeclaration(ctx.typeDeclaration());
    if (ctx.letDeclaration() != null)
      return visitLetDeclaration(ctx.letDeclaration());
    if (ctx.expression() != null)
      return visitExpression(ctx.expression());
    throw new InternalError(String.format("Could not recognize CompoundElement: %s",
        ctx.getText()));
  }
  @Override
  public Tree visitCompoundExpression(JujanParser.CompoundExpressionContext ctx) {
    if (ctx == null)
      return null;
    List<Tree.CompoundExpressionElement> xs = ctx.compoundExpressionElement().stream().map
        (this::visitCompoundExpressionElement)
        .collect(Collectors.toList());
    xs.add(visitExpression(ctx.lastExpression));
    return factory.newCompoundExpression(
        location(ctx),
        xs,
        ctx.lastSemicolon != null);
  }
  @Override
  public Tree.ThrowExpression visitThrowExpression(JujanParser.ThrowExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newThrowExpression(location(ctx), visitExpression(ctx.expression()));
  }
  @Override
  public Tree.ReturnExpression visitReturnExpression(JujanParser.ReturnExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newReturnExpression(
        location(ctx),
        visitQualifiedIdentifier(ctx.qualifiedIdentifier()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.DoneExpression visitDoneExpression(JujanParser.DoneExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newDoneExpression(
        location(ctx),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.DefineDeclaration visitDeclaration(JujanParser.DeclarationContext ctx) {
    if (ctx == null)
      return null;
    return factory.newDefineDeclaration(
        location(ctx),
        ctx.generic != null,
        visitIdentifier(ctx.identifier()),
        visitTypeExpression(ctx.typeExpression()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.LetDeclaration visitLetDeclaration(JujanParser.LetDeclarationContext ctx) {
    if (ctx == null)
      return null;
    return factory.newLetDeclaration(
        location(ctx),
        visitIdentifier(ctx.identifier()),
        visitTypeExpression(ctx.typeExpression()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.SlotDeclaration visitSlotDeclaration(JujanParser.SlotDeclarationContext ctx) {
    if (ctx == null)
      return null;
    return factory.newSlotDeclaration(
        location(ctx),
        visitIdentifier(ctx.identifier()),
        visitTypeExpression(ctx.typeExpression()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.SlotDereferernceExpression visitSlotDereferernceExpression(
      JujanParser.SlotDereferernceExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newSlotDereferernceExpression(
        location(ctx),
        visitQualifiedIdentifier(ctx.qualifiedIdentifier()));
  }
  @Override
  public Tree.SlotAssignmentExpression visitSlotAssignmentExpression(
      JujanParser.SlotAssignmentExpressionContext ctx) {
    if (ctx == null)
      return null;
    return factory.newSlotAssignmentExpression(
        location(ctx),
        visitSlotDereferernceExpression(ctx.slotDereferernceExpression()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.DeclarationOrExpression visitModuleComponent(JujanParser.ModuleComponentContext ctx) {
    if (ctx == null)
      return null;
    val ret = visitChildren(ctx);
    if (!(ret instanceof Tree.DeclarationOrExpression))
      throw new InternalError(String.format("%s is parsed to %s but expected Exp", ctx
          .getText(), ret.toString()));
    return (Tree.DeclarationOrExpression) ret;
  }
  @Override
  public Tree visitModule(JujanParser.ModuleContext ctx) {
    if (ctx == null)
      return null;
    return factory.newModule(
        location(ctx),
        ctx.Shebang() == null ? null : ctx.Shebang().getText(),
        ctx.Lang() == null ? null : ctx.Lang().getText(),
        ctx.Import().stream().map(TerminalNode::getText).collect(Collectors.toList()),
        ctx.moduleComponent().stream().map(this::visitModuleComponent).collect(Collectors.toList
            ()));
  }
  @Override
  public Tree.ApplicationParameter visitApplicationParameter(
      JujanParser.ApplicationParameterContext ctx) {
    if (ctx == null)
      return null;
    return factory.newApplicationParameter(location(ctx), visitIdentifier(ctx.identifier()),
        visitExpression(ctx.expression()));
  }
  @Override
  public Tree.Expression visitExpression(JujanParser.ExpressionContext ctx) {
    if (ctx == null)
      return null;
    val l = location(ctx);
    if (ctx.binaryOperator != null) {
      val t = ctx.binaryOperator.getText();
      return factory.newBinaryOperationExpression(
          l,
          visitExpression(ctx.left),
          factory.newIdentifier(location(ctx.binaryOperator), t.startsWith("'") ? t.substring(1,
              t.length() - 1) : t),
          visitExpression(ctx.right));
    }
    if (ctx.unaryOperator != null) {
      val t = ctx.unaryOperator.getText();
      return factory.newUnaryOperationExpression(
          l,
          factory.newIdentifier(location(ctx.unaryOperator), t.startsWith("'") ? t.substring(1,
              t.length() - 1) : t),
          visitExpression(ctx.operand));
    }
    if (ctx.indexedExpressoin != null) {
      return factory.newIndexingExpression(
          l,
          visitExpression(ctx.indexedExpressoin),
          visitExpression(ctx.index));
    }
    if (ctx.appliedFunction != null) {
      return factory.newApplicationExpression(
          l,
          visitExpression(ctx.appliedFunction),
          ctx.applicationParameter().stream().map(this::visitApplicationParameter).collect
              (Collectors.toList()));
    }
    val ret = visitChildren(ctx);
    if (!(ret instanceof Tree.Expression))
      throw new InternalError(String.format("%s is parsed to %s but expected Exp", ctx
          .getText(), ret.toString()));
    return (Tree.Expression) ret;
  }
}
