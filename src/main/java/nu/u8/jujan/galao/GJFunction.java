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

import fj.P2;
import lombok.*;
import nu.u8.jujan.galao.internal.StreamUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJFunction extends GJObject {
  @Getter(AccessLevel.PRIVATE)
  List<P2<String, GJObject>> captured;
  GJLambda code;
  @Getter(lazy = true)
  private final transient Extension captureExtension = new Extension(captured.stream().map(P2::_1));
  Stream<String> parameters() {
    return StreamUtil.fromIterator(code.getParameters().iterator())
        .map(p -> p.getIdentifier().getName());
  }
  @Getter(lazy = true)
  private final transient List<Resolution> parameterResolver =
      parameters().map(Resolution::new).collect(Collectors.toList());
  @Getter(lazy = true)
  private final transient Extension parameterExtension =
      new Extension(parameters());
  public GJObject apply(GJObject env, GJObject args) {
    try {
      GJObject e1 = getCaptureExtension().extend(env, captured.stream().map(P2::_2));
      GJObject e2 = getParameterExtension().extend(e1, getParameterResolver().stream()
          .map(r -> r.resolve(args)));
      return code.getBody().eval(e2);
    } catch (Return e) {
      if (e.getTarget() == null || e.getTarget() == this)
        return e.getValue();
      throw e;
    }
  }
}
