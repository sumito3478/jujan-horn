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

import fj.data.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.val;

import java.util.stream.Stream;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJLet extends GJExpression {
  GJLocation location;
  GJIdentifier name;
  GJExpression value;
  GJExpression body;
  boolean exported;
  @Getter(lazy = true)
  private final transient GJObject.Extension extension =
      new GJObject.Extension(Stream.of(name.getName()));
  public GJObject eval(GJObject env) {
    val newEnv = getExtension().extend(env, Stream.of(value.eval(env)));
    return body.eval(newEnv);
  }
  Set<String> capturing(Set<String> env, Set<String> captured) {
    return body.capturing(env, value.capturing(env, name.capturing(env, captured)));
  }

}
