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

import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.List;

interface Tree {
  Location getLocation();
  <A> A visit(Visitor<A> visitor);
  interface Visitor<A> {
    A visit(NilLiteral tree);
    A visit(BooleanLiteral tree);
    A visit(Int32Literal tree);
    A visit(BigIntLiteral tree);
    A visit(TextLiteral tree);
    A visit(Identifier tree);
    A visit(QualifiedIdentifier tree);
    A visit(RecordTypeExpression tree);
    A visit(TypeExpression tree);
    A visit(ListTypeExpression tree);
    A visit(TypeOfTypeExpression tree);
    A visit(BinaryOperationTypeExpression tree);
    A visit(QualifiedTypeIdentifier tree);
    A visit(TypeDeclaration tree);
    A visit(LambdaExpressionParameter tree);
    A visit(LambdaExpression tree);
    A visit(RecordExpression tree);
    A visit(ListExpression tree);
    A visit(CompoundExpression tree);
    A visit(ThrowExpression tree);
    A visit(ReturnExpression tree);
    A visit(DoneExpression tree);
    A visit(DefineDeclaration tree);
    A visit(LetDeclaration tree);
    A visit(SlotDeclaration tree);
    A visit(SlotDereferernceExpression tree);
    A visit(SlotAssignmentExpression tree);
    A visit(BinaryOperationExpression tree);
    A visit(IndexingExpression tree);
    A visit(ApplicationParameter tree);
    A visit(ApplicationExpression tree);
    A visit(UnaryOperationExpression tree);
    default A visit(Tree tree) {
      return tree.visit(this);
    }
  }
  interface Factory {
    NilLiteral newNilLiteral(
        Location location
    );
    BooleanLiteral newBooleanLiteral(
        Location location,
        boolean value
    );
    Int32Literal newInt32Literal(
        Location location,
        int value
    );
    BigIntLiteral newBigIntLiteral(
        Location location,
        BigInteger value
    );
    TextLiteral newTextLiteral(
        Location location,
        String value
    );
    Identifier newIdentifier(
        Location location,
        String name
    );
    QualifiedIdentifier newQualifiedIdentifier(
        Location location,
        List<? extends Identifier> components
    );
    RecordTypeExpression newRecordTypeExpression(
        Location location,
        List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
            fields
    );
    LambdaTypeExpression newLambdaTypeExpression(
        Location location,
        List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
            parameters,
        TypeExpression resultType
    );
    ListTypeExpression newListTypeExpression(
        Location location,
        List<? extends TypeExpression> types,
        boolean lastStar
    );
    TypeOfTypeExpression newTypeOfTypeExpression(
        Location location,
        QualifiedIdentifier operand
    );
    BinaryOperationTypeExpression newBinaryOperationTypeExpression(
        Location location,
        TypeExpression left,
        Identifier operator,
        TypeExpression right
    );
    QualifiedTypeIdentifier newQualifiedTypeIdentifier(
        Location location,
        List<? extends Identifier> components
    );
    TypeDeclaration newTypeDeclaration(
        Location location,
        Identifier name,
        Identifier operator,
        TypeExpression body
    );
    LambdaExpressionParameter newLambdaExpressionParameter(
        Location location,
        Identifier name,
        TypeExpression typeAnnotation,
        Expression defaultValue
    );
    LambdaExpression newLambdaExpression(
        Location location,
        List<? extends LambdaExpressionParameter> parameters,
        TypeExpression typeConstraint,
        Expression body
    );
    RecordExpression newRecordExpression(
        Location location,
        List<? extends Pair<? extends Identifier, ? extends Expression>> fields
    );
    ListExpression newListExpression(
        Location location,
        List<? extends Expression> elements
    );
    CompoundExpression newCompoundExpression(
        Location location,
        List<? extends CompoundExpressionElement> elements,
        boolean lastSemicolon
    );
    ThrowExpression newThrowExpression(
        Location location,
        Expression operand
    );
    ReturnExpression newReturnExpression(
        Location location,
        @Nullable
        QualifiedIdentifier destination,
        @Nullable
        Expression returnValue
    );
    DoneExpression newDoneExpression(
        Location location,
        @Nullable
        Expression returnValue
    );
    DefineDeclaration newDefineDeclaration(
        Location location,
        boolean generic,
        Identifier name,
        TypeExpression type,
        Expression body
    );
    LetDeclaration newLetDeclaration(
        Location location,
        Identifier name,
        TypeExpression type,
        Expression body
    );
    SlotDeclaration newSlotDeclaration(
        Location location,
        Identifier name,
        TypeExpression type,
        Expression body
    );
    SlotDereferernceExpression newSlotDereferernceExpression(
        Location location,
        QualifiedIdentifier target
    );
    SlotAssignmentExpression newSlotAssignmentExpression(
        Location location,
        SlotDereferernceExpression left,
        Expression right
    );
    BinaryOperationExpression newBinaryOperationExpression(
        Location location,
        Expression left,
        Identifier operator,
        Expression right
    );
    IndexingExpression newIndexingExpression(
        Location location,
        Expression left,
        Expression right
    );
    ApplicationParameter newApplicationParameter(
        Location location,
        @Nullable
        Identifier name,
        Expression value
    );
    ApplicationExpression newApplicationExpression(
        Location location,
        Expression function,
        List<? extends ApplicationParameter> parameters
    );
    UnaryOperationExpression newUnaryOperationExpression(
        Location location,
        Identifier operator,
        Expression operand
    );
    Module newModule(
        Location location,
        @Nullable
        String shebangLine,
        @Nullable
        String langLine,
        List<? extends String> importLines,
        List<? extends DeclarationOrExpression> components
    );
  }
  interface CompoundExpressionElement extends Tree {

  }
  interface DeclarationOrExpression extends Tree {

  }
  interface Declaration extends DeclarationOrExpression {

  }
  interface Expression extends CompoundExpressionElement, DeclarationOrExpression {
  }
  interface NilLiteral extends Expression {
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface BooleanLiteral extends Expression {
    boolean getValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface Int32Literal extends Expression {
    int getValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface BigIntLiteral extends Expression {
    BigInteger getValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface TextLiteral extends Expression {
    String getValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface Identifier extends Expression {
    String getName();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface QualifiedIdentifier extends Expression {
    List<? extends Identifier> getComponents();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface TypeExpression extends Tree {

  }
  interface RecordTypeExpression extends TypeExpression {
    List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
    getFields();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface LambdaTypeExpression extends TypeExpression {
    List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
    getParameters();
    TypeExpression getResultType();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface ListTypeExpression extends TypeExpression {
    List<? extends TypeExpression> getTypes();
    boolean isEndingWithStar();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface TypeOfTypeExpression extends TypeExpression {
    QualifiedIdentifier getOperand();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface BinaryOperationTypeExpression extends TypeExpression {
    TypeExpression getLeft();
    Identifier getOperator();
    TypeExpression getRight();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface QualifiedTypeIdentifier extends TypeExpression {
    List<? extends Identifier> getComponents();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface TypeDeclaration extends CompoundExpressionElement {
    Identifier getName();
    Identifier getOperator();
    TypeExpression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface LambdaExpressionParameter extends Tree {
    Identifier getName();
    TypeExpression getTypeAnnotation();
    Expression getDefaultValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface LambdaExpression extends Expression {
    List<? extends LambdaExpressionParameter> getParameters();
    TypeExpression getTypeConstraint();
    Expression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface RecordExpression extends Expression {
    List<? extends Pair<? extends Identifier, ? extends Expression>>
    getFields();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface ListExpression extends Expression {
    List<? extends Expression> getElements();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface CompoundExpression extends Expression {
    List<? extends CompoundExpressionElement> getElements();
    boolean isEndingWithSemicolon();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }

  interface ThrowExpression extends Expression {
    Expression getOperand();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface ReturnExpression extends Expression {
    @Nullable
    QualifiedIdentifier getDestination();
    @Nullable
    Expression getReturnValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface DoneExpression extends Expression {
    @Nullable
    Expression getReturnValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface DefineDeclaration extends CompoundExpressionElement, Declaration {
    boolean isGeneric();
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface LetDeclaration extends CompoundExpressionElement, Declaration {
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface SlotDeclaration extends Declaration {
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface SlotDereferernceExpression extends Expression {
    QualifiedIdentifier getTarget();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface SlotAssignmentExpression extends Expression {
    SlotDereferernceExpression getLeft();
    Expression getRight();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface BinaryOperationExpression extends Expression {
    Expression getLeft();
    Identifier getOperator();
    Expression getRight();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface IndexingExpression extends Expression {
    Expression getLeft();
    Expression getRight();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface ApplicationParameter extends Tree {
    @Nullable
    Identifier getName();
    Expression getValue();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface ApplicationExpression extends Expression {
    Expression getFunction();
    List<? extends ApplicationParameter> getParameters();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface UnaryOperationExpression extends Expression {
    Identifier getOperator();
    Expression getOperand();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface Module extends Tree {
    @Nullable
    String getShebangLine();
    @Nullable
    String getLangLine();
    List<? extends String> getImportLines();
    List<? extends DeclarationOrExpression> getComponents();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  @Value(staticConstructor = "of")
  class Location {
    private final String fileName;
    private final int line;
    private final int column;
  }
}
