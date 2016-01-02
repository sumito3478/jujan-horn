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
import lombok.Getter;
import lombok.Value;

import javax.annotation.Nullable;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJLazy extends GJObject {
  GJObject env;
  GJExpression body;
  @Getter(lazy = true)
  private final transient GJObject value = body.eval(this.env);
  public GJObject eval(GJObject env) {
    return getValue();
  }
  Set<String> capturing(Set<String> env, Set<String> captured) {
    return body.capturing(env, captured);
  }

}
