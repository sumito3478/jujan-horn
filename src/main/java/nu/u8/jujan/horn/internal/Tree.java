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

package nu.u8.jujan.horn.internal;

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
    A visit(Declaration tree);
    A visit(LetDeclaration tree);
    A visit(SlotDeclaration tree);
    A visit(SlotDereferernceExpression tree);
    A visit(SlotAssignmentExpression tree);
    A visit(BinaryOperationExpression tree);
    A visit(IndexingExpression tree);
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
        boolean value
    );
    Int32Literal newInt32Literal(
        int value
    );
    BigIntLiteral newBigIntLiteral(
        BigInteger value
    );
    TextLiteral newTextLiteral(
        String value
    );
    Identifier newIdentifier(
        String name
    );
    QualifiedIdentifier newQualifiedIdentifier(
        List<? extends Identifier> components
    );
    RecordTypeExpression newRecordTypeExpression(
        List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
            fields
    );
    LambdaTypeExpression newTypeExpression(
        List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
            parameters,
        TypeExpression resultType
    );
    ListTypeExpression newListTypeExpression(
        List<? extends TypeExpression> types,
        boolean lastStar
    );
    TypeOfTypeExpression newTypeOfTypeExpression(
        QualifiedIdentifier operand
    );
    BinaryOperationTypeExpression newBinaryOperationTypeExpression(
        TypeExpression left,
        Identifier operator,
        TypeExpression right
    );
    QualifiedTypeIdentifier newQualifiedTypeIdentifier(
        List<? extends Identifier> components
    );
    TypeDeclaration newTypeDeclaration(
        Identifier name,
        Identifier operator,
        TypeExpression body
    );
    LambdaExpressionParameter newLambdaExpressionParameter(
        Identifier name,
        TypeExpression typeAnnotation,
        Expression defaultValue
    );
    LambdaExpression newLambdaExpression(
        List<? extends LambdaExpressionParameter> parameters,
        TypeExpression typeConstraint,
        Expression body
    );
    RecordExpression newRecordExpression(
        List<? extends Pair<? extends Identifier, ? extends Expression>> fields
    );
    ListExpression newListExpression(
        List<? extends Expression> elements
    );
    CompoundExpression newCompoundExpression(
        List<? extends CompoundExpressionElement> elements,
        boolean lastSemicolon
    );
    ThrowExpression newThrowExpression(
        Expression operand
    );
    ReturnExpression newReturnExpression(
        @Nullable
        QualifiedIdentifier destination,
        @Nullable
        Expression returnValue
    );
    DoneExpression newDoneExpression(
        @Nullable
        Expression returnValue
    );
    Declaration newDeclaration(
        boolean generic,
        Identifier name,
        TypeExpression type,
        Expression body
    );
    LetDeclaration newLetDeclaration(
        Identifier name,
        TypeExpression type,
        Expression body
    );
    SlotDeclaration newSlotDeclaration(
        Identifier name,
        TypeExpression type,
        Expression body
    );
    SlotDereferernceExpression newSlotDereferernceExpression(
        QualifiedIdentifier target
    );
    SlotAssignmentExpression newSlotAssignmentExpression(
        SlotDereferernceExpression left,
        Expression right
    );
    BinaryOperationExpression newBinaryOperationExpression(
        Expression left,
        Identifier operator,
        Expression right
    );
    IndexingExpression newIndexingExpression(
        Expression left,
        Expression right
    );
    ApplicationExpression newApplicationExpression(
        Expression function,
        List<? extends Pair<? extends Identifier, ? extends Expression>> parameters
    );
    UnaryOperationExpression newUnaryOperationExpression(
        Identifier operator,
        Expression operand
    );

  }
  interface CompoundExpressionElement extends Tree {

  }
  interface Expression extends CompoundExpressionElement {
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
    boolean hasLastStar();
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
  interface TypeDeclaration extends Tree {
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
    boolean hasLastSemicolon();
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
  interface Declaration extends CompoundExpressionElement {
    boolean isGeneric();
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface LetDeclaration extends CompoundExpressionElement {
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
    default <A> A visit(Visitor<A> visitor) {
      return visitor.visit(this);
    }
  }
  interface SlotDeclaration extends Tree {
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
  interface ApplicationExpression extends Expression {
    Expression getFunction();
    List<? extends Pair<? extends Identifier, ? extends Expression>> getParameters();
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
  @Value(staticConstructor = "of")
  class Location {
    private final String fileName;
    private final int line;
    private final int column;
  }
}
