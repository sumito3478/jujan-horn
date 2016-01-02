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

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.annotation.Nullable;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJReturn extends GJExpression {
  GJLocation location;
  @Nullable
  GJExpression target;
  @Nullable
  GJExpression value;
  @Override
  public GJObject eval(GJObject env) {
    throw new Return(target == null ? null : (GJFunction)target.eval(env),
        value == null ? new GJNil(location) : value.eval(env));
  }
}
