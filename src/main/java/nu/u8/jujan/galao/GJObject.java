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
import fj.Ord;
import fj.data.TreeMap;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.annotation.Nullable;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GJObject {
  private final TreeMap<String, Link> shape;
  private final GJObject[][] slots;
  @Getter
  @Nullable
  private final GJObject prototype;
  protected GJObject() {
    this(TreeMap.empty(Ord.stringOrd), new GJObject[][] { { } }, null);
  }
  public static final GJObject empty = new GJObject();
  @Value
  public static class Link {
    int _1;
    int _2;
  }
  @RequiredArgsConstructor
  @EqualsAndHashCode
  public static class Resolution {
    private final String name;
    @Nullable
    private TreeMap<String, Link> shape = null;
    @Nullable
    private Link link = null;
    public GJObject resolve(GJObject x) {
      if (link == null || shape == null || shape != x.shape) {
        shape = x.shape;
        link = shape.get(name).orSome(() -> { throw new InternalError(); });
      }
      return x.slots[link._1][link._2];
    }
  }
  @EqualsAndHashCode
  public static class Extension {
    private final String[] names;
    @Nullable
    private TreeMap<String, Link> prototypeShape = null;
    @Nullable
    private TreeMap<String, Link> shape = null;
    public Extension(Stream<String> names) {
      this.names = names.toArray(String[]::new);
    }
    public GJObject extend(@Nullable GJObject prototype, Stream<GJObject> values) {
      val ps = prototype == null ? null : prototype.shape;
      val depth = prototype == null ? 0 : prototype.slots.length;
      if (shape != null && ps == prototypeShape) {
        val newSlots = new GJObject[depth + 1][];
        if (prototype != null)
          System.arraycopy(prototype.slots, 0, newSlots, 0, depth);
        newSlots[depth] = values.toArray(GJObject[]::new);
        return new GJObject(shape, newSlots, prototype);
      }
      shape = ps == null ?
          TreeMap.empty(Ord.stringOrd) :
          ps;
      for (int i = 0; i < names.length; ++i)
        shape = shape.set(names[i], new Link(depth, i));
      prototypeShape = ps;
      return extend(prototype, values);
    }
  }
  @Override
  public String toString() {
    val builder = new StringBuilder();
    builder.append('{');
    shape.forEach(f -> {
      builder.append('"');
      builder.append(f._1().replace("\"", "\\\""));
      builder.append("\" : ");
      builder.append(slots[f._2()._1][f._2()._2]);
      builder.append(", ");
    });
    builder.append('}');
    return builder.toString();
  }
}
