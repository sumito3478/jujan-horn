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
import lombok.val;

import javax.annotation.Nullable;
import java.math.BigInteger;

public interface ImmutableTree extends Tree {
  Tree.Factory factory = TreeUtil.createFactoryImplementation(ImmutableTree.class);

  interface CompoundElement extends Tree.CompoundExpressionElement, ImmutableTree {

  }
  interface DorE extends Tree.DeclarationOrExpression, ImmutableTree {

  }
  interface Decl extends Tree.Declaration, ImmutableTree {

  }
  interface Exp extends Tree.Expression, CompoundElement, DorE {
  }
  interface TExp extends Tree.TypeExpression, ImmutableTree {
  }
  @Value(staticConstructor = "of")
  class Nil implements ImmutableTree, Exp, Tree.NilLiteral {
    private final Location location;
  }
  @Value(staticConstructor = "of")
  class Bool implements ImmutableTree, Exp, Tree.BooleanLiteral {
    private final Location location;
    private final boolean value;
    public Bool(Location location, boolean value) {
      this.location = location;
      this.value = value;
    }
    public Location getLocation() {
      return location;
    }
    public boolean getValue() {
      return value;
    }
    public static Bool of(Location location, boolean value) {
      return new Bool(location, value);
    }
  }
  @Value(staticConstructor = "of")
  class Int32 implements ImmutableTree, Exp, Tree.Int32Literal {
    private final Location location;
    private final int value;

  }
  @Value(staticConstructor = "of")
  class BigInt implements ImmutableTree, Exp, Tree.BigIntLiteral {
    private final Location location;
    private final BigInteger value;

  }
  @Value(staticConstructor = "of")
  class Text implements ImmutableTree, Exp, Tree.TextLiteral {
    private final Location location;
    private final String value;

  }
  @Value(staticConstructor = "of")
  class Id implements ImmutableTree, Exp, Tree.Identifier {
    private final Location location;
    private final String name;

  }
  @Value(staticConstructor = "of")
  class QId implements ImmutableTree, Exp, Tree.QualifiedIdentifier {
    private final Location location;
    java.util.List components;

  }
  @Value(staticConstructor = "of")
  class RecordTExp implements ImmutableTree, TExp, Tree.RecordTypeExpression {
    private final Location location;
    java.util.List fields;

  }
  @Value(staticConstructor = "of")
  class LambdaTExp implements ImmutableTree, TExp, Tree.LambdaTypeExpression {
    private final Location location;
    java.util.List parameters;
    private final TExp resultType;

  }
  @Value(staticConstructor = "of")
  class ListTExp implements ImmutableTree, TExp, Tree.ListTypeExpression {
    private final Location location;
    java.util.List types;
    boolean endingWithStar;

  }
  @Value(staticConstructor = "of")
  class TypeOfTExp implements ImmutableTree, TExp, Tree.TypeOfTypeExpression {
    private final Location location;
    private final QId operand;

  }
  @Value(staticConstructor = "of")
  class BinaryOperationTExp implements ImmutableTree, TExp, Tree.BinaryOperationTypeExpression {
    private final Location location;
    private final TExp left;
    private final Id operator;
    private final TExp right;

  }
  @Value(staticConstructor = "of")
  class QTId implements ImmutableTree, TExp, Tree.QualifiedTypeIdentifier {
    private final Location location;
    java.util.List components;

  }
  @Value(staticConstructor = "of")
  class TypeDecl implements ImmutableTree, CompoundElement, Tree.TypeDeclaration {
    private final Location location;
    private final Id name;
    private final Id operator;
    private final TExp body;

  }
  @Value(staticConstructor = "of")
  class LambdaExpParam implements ImmutableTree, Tree, Tree.LambdaExpressionParameter {
    private final Location location;
    private final Id name;
    private final TExp typeAnnotation;
    Exp defaultValue;
  }
  @Value(staticConstructor = "of")
  class Lambda implements ImmutableTree, Exp, Tree.LambdaExpression {
    private final Location location;
    java.util.List parameters;
    private final TExp typeConstraint;
    private final Exp body;

  }
  @Value(staticConstructor = "of")
  class Record implements ImmutableTree, Exp, Tree.RecordExpression {
    private final Location location;
    java.util.List fields;

  }
  @Value(staticConstructor = "of")
  class List implements ImmutableTree, Exp, Tree.ListExpression {
    private final Location location;
    java.util.List elements;

  }
  @Value(staticConstructor = "of")
  class Compound implements ImmutableTree, Exp, Tree.CompoundExpression {
    private final Location location;
    java.util.List elements;
    boolean endingWithSemicolon;

  }

  @Value(staticConstructor = "of")
  class Throw implements ImmutableTree, Exp, Tree.ThrowExpression {
    private final Location location;
    private final Exp operand;

  }
  @Value(staticConstructor = "of")
  class Return implements ImmutableTree, Exp, Tree.ReturnExpression {
    private final Location location;
    @Nullable
    private final QId destination;
    @Nullable
    private final Exp returnValue;

  }
  @Value(staticConstructor = "of")
  class Done implements ImmutableTree, Exp, Tree.DoneExpression {
    private final Location location;
    @Nullable
    private final Exp returnValue;

  }
  @Value(staticConstructor = "of")
  class DefineDecl implements CompoundElement, Decl, Tree.DefineDeclaration {
    private final Location location;
    private final boolean generic;
    private final Id name;
    private final TExp type;
    private final Exp body;

  }
  @Value(staticConstructor = "of")
  class LetDecl implements
      CompoundElement, Decl, Tree.LetDeclaration {
    private final Location location;
    private final Id name;
    private final TExp type;
    private final Exp body;

  }
  @Value(staticConstructor = "of")
  class SlotDecl implements ImmutableTree, Decl, Tree.SlotDeclaration {
    private final Location location;
    private final Id name;
    private final TExp type;
    private final Exp body;

  }
  @Value(staticConstructor = "of")
  class SlotDereferernce implements ImmutableTree, Exp, Tree.SlotDereferernceExpression {
    private final Location location;
    private final QId target;

  }
  @Value(staticConstructor = "of")
  class SlotAssignment implements ImmutableTree, Exp, Tree.SlotAssignmentExpression {
    private final Location location;
    private final SlotDereferernce left;
    private final Exp right;

  }
  @Value(staticConstructor = "of")
  class BinaryOperation implements ImmutableTree, Exp, Tree.BinaryOperationExpression {
    private final Location location;
    private final Exp left;
    private final Id operator;
    private final Exp right;

  }
  @Value(staticConstructor = "of")
  class Indexing implements ImmutableTree, Exp, Tree.IndexingExpression {
    private final Location location;
    private final Exp left;
    private final Exp right;

  }
  @Value(staticConstructor = "of")
  class ApplicationParam implements ImmutableTree, Tree, Tree.ApplicationParameter {
    private final Location location;
    @Nullable
    private final Id name;
    private final Exp value;

  }
  @Value(staticConstructor = "of")
  class Application implements ImmutableTree, Exp, Tree.ApplicationExpression {
    private final Location location;
    private final Exp function;
    java.util.List parameters;

  }
  @Value(staticConstructor = "of")
  class UnaryOperation implements ImmutableTree, Exp, Tree.UnaryOperationExpression {
    private final Location location;
    private final Id operator;
    private final Exp operand;

  }
  @Value(staticConstructor = "of")
  class M implements ImmutableTree, Tree.Module {
    private final Location location;
    @Nullable
    private final String shebangLine;
    @Nullable
    private final String langLine;
    java.util.List importLines;
    java.util.List components;
  }
}