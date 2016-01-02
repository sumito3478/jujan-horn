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

import fj.P2;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Value
@EqualsAndHashCode(callSuper = true)
public class GJLocal extends GJExpression {
  GJLocation location;
  @NonFinal
  transient GJObject.Extension extension = null;
  public GJObject eval(GJObject env) {
    List<P2<String, GJObject>> fields = env.stream().filter(x -> !x._1().startsWith("_"))
        .collect(Collectors.toList());
    String[] names = fields.stream().map(P2::_1).toArray(String[]::new);
    if (extension == null || Arrays.equals(names, extension.names))
      extension = new GJObject.Extension(Arrays.stream(names));
    return extension.extend(null, fields.stream().map(P2::_2));
  }
  public Set<String> capturing(Set<String> env, Set<String> captured) {
    // local captures everything that is available
    throw new ExpressionCapturesEverything();
  }
}
