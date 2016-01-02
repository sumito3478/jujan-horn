// Copyright (C) 2016 Tomoaki Takezoe (a.k.a @sumito3478) <sumito3478@gmail.com>
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

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJCompoundExpression extends GJExpression {
  GJLocation location;
  @Getter(value = AccessLevel.PRIVATE)
  GJExpression[] expressions;
  boolean endsWithSemicolon;
  public GJObject eval(GJObject env) {
    GJObject ret = null;
    for (int i = 0; i < expressions.length; ++i)
      ret = expressions[i].eval(env);
    return endsWithSemicolon ? new GJNil(location) : ret;
  }

}
