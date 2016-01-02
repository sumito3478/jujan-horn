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
import fj.P2;
import fj.data.List;
import fj.data.Set;
import fj.data.TreeMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.val;
import nu.u8.jujan.galao.internal.StreamUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
@EqualsAndHashCode(callSuper = true)
public class GJLambda extends GJExpression {
  GJLocation location;
  List<GJLambdaParameter> parameters;
  GJExpression body;
  @NonFinal
  transient Set<String> captured = null;
  @NonFinal
  transient boolean captureAll = false;
  public GJObject eval(GJObject env) {
    if (!captureAll && captured == null) {
      try {
        captured = body.capturing(Set.empty(Ord.stringOrd), Set.empty((Ord.stringOrd)));
      } catch (ExpressionCapturesEverything e) {
        captureAll = true;
      }
    }
    if (captureAll)
      return new GJFunction(env, this);
    return new GJFunction(GJObject.fromStream(env.stream().filter(f -> captured.member(f._1()))),
        this);
  }
  Set<String> capturing(Set<String> env, Set<String> captured) {
    return body.capturing(env, captured);
  }
}
