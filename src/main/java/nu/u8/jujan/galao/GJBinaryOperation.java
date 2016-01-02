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
package nu.u8.jujan.galao;
import fj.Ord;
import fj.P;
import fj.data.List;
import fj.data.Set;
import fj.data.TreeMap;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJBinaryOperation extends GJExpression {
  GJLocation location;
  GJIdentifier operator;
  GJExpression left;
  GJExpression right;
  private static TreeMap<String, Link> parameterShape =
      TreeMap.treeMap(Ord.stringOrd, List.list(P.p("left", new Link(0, 0)),
          P.p("right", new Link(0, 1))));
  public GJObject eval(GJObject env) {
    val f = operator.eval(env);
    val l = left.eval(env);
    val r = right.eval(env);
    if (!(f instanceof GJFunction))
      throw new InternalError("Not a function");
    return ((GJFunction)f).apply(env, new GJObject(parameterShape,
          new GJObject[][] { new GJObject[] { l, r }}, null));
  }
  Set<String> capturing(Set<String> env, Set<String> captured) {
    return right.capturing(env, left.capturing(env, operator.capturing(env, captured)));
  }
}
