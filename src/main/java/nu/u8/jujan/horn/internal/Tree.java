package nu.u8.jujan.horn.internal;

import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.List;

interface Tree {
  Location getLocation();
  interface Factory {

  }
  @Value(staticConstructor = "of")
  class Location {
    private final String fileName;
    private final int line;
    private final int column;
  }
  interface CompoundExpressionElement extends Tree {

  }
  interface Expression extends CompoundExpressionElement {
  }
  interface NilLiteral extends Expression {
  }
  interface BooleanLiteral extends Expression {
    boolean getValue();
  }
  interface Int32Literal extends Expression {
    int getValue();
  }
  interface BigIntLiteral extends Expression {
    BigInteger getValue();
  }
  interface TextLiteral extends Expression {
    String getValue();
  }
  interface Identifier extends Expression {
    String getName();
  }
  interface QualifiedIdentifier extends Expression {
    List<? extends Identifier> getComponents();
  }
  interface TypeExpression extends Tree {

  }
  interface RecordTypeExpression extends TypeExpression {
    List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
    getFields();
  }
  interface LambdaTypeExpression extends TypeExpression {
    List<? extends Pair<? extends Identifier, ? extends TypeExpression>>
    getParameters();
    TypeExpression getResultType();
  }
  interface ListTypeExpression extends TypeExpression {
    List<? extends TypeExpression> getTypes();
    boolean hasLastStar();
  }
  interface TypeOfTypeExpression extends TypeExpression {
    QualifiedIdentifier getOperand();
  }
  interface BinaryOperationTypeExpression extends TypeExpression {
    TypeExpression getLeft();
    Identifier getOperator();
    TypeExpression getRight();
  }
  interface QualifiedTypeIdentifier extends TypeExpression {
    List<? extends Identifier> getComponents();
  }
  interface TypeDeclaration extends Tree {
    Identifier getName();
    Identifier getOperator();
    TypeExpression getBody();
  }
  interface LambdaExpressionParameter extends Tree {
    Identifier getName();
    TypeExpression getTypeAnnotation();
    Expression getDefaultValue();
  }
  interface LambdaExpression extends Expression {
    List<? extends LambdaExpressionParameter> getParameters();
    TypeExpression getTypeConstraint();
    Expression getBody();
  }
  interface RecordExpression extends Expression {
    List<? extends Pair<? extends Identifier, ? extends Expression>>
    getFields();
  }
  interface ListExpression extends Expression {
    List<? extends Expression> getElements();
  }

  interface CompoundExpression extends Expression {
    List<? extends CompoundExpressionElement> getElements();
    boolean hasLastSemicolon();
  }
  interface ThrowExpression extends Expression {
    Expression getOperand();
  }
  interface ReturnExpression extends Expression {
    @Nullable QualifiedIdentifier getDestination();
    @Nullable Expression getReturnValue();
  }
  interface DoneExpression extends Expression {
    @Nullable Expression getReturnValue();
  }
  interface Declaration extends CompoundExpressionElement {
    boolean isGeneric();
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
  }
  interface LetDeclaration extends CompoundExpressionElement {
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
  }
  interface SlotDeclaration extends Tree {
    Identifier getName();
    TypeExpression getType();
    Expression getBody();
  }
  interface SlotDereferernceExpression extends Expression {
    QualifiedIdentifier getTarget();
  }
  interface SlotAssignmentExpression extends Expression {
    SlotDereferernceExpression getLeft();
    Expression getRight();
  }
  interface BinaryOperationExpression extends Expression {
    Expression getLeft();
    Identifier getOperator();
    Expression getRight();
  }
  interface IndexingExpression extends Expression {
    Expression getLeft();
    Expression getRight();
  }
  interface ApplicationExpression extends Expression {
    Expression getFunction();
    List<? extends Pair<? extends Identifier, ? extends Expression>>
        getParameters();
  }
  interface UnaryOperationExpression extends Expression {
    Identifier getOperator();
    Expression getOperand();
  }
}
