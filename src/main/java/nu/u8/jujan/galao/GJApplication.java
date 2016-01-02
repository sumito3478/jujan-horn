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

import fj.data.Set;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJApplication extends GJExpression {
  GJLocation location;
  GJExpression left;
  GJExpression right;
  boolean isSingleParameter;
  transient GJObject.Extension extension = null;
  public GJObject eval(GJObject env) {
    val f = left.eval(env);
    val args = right.eval(env);
    if (!(f instanceof GJFunction))
      throw new InternalError("Not a function");
    return ((GJFunction) f).apply(env, args);
  }
  Set<String> capturing(Set<String> env, Set<String> captured) {
    return right.capturing(env, left.capturing(env, captured));
  }
}
