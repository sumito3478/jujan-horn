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

package nu.u8.jujan.galao.internal;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.BaseStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
public class StreamUtil {
  public static <A, B> Stream<Pair<A, B>> zip(BaseStream<A, ?> xs, BaseStream<B, ?> ys) {
    val it1 = xs.iterator();
    val it2 = ys.iterator();
    val it3 = new Iterator<Pair<A, B>>() {
      @Override
      public boolean hasNext() {
        return it1.hasNext() && it2.hasNext();
      }
      @Override
      public Pair<A, B> next() {
        return new ImmutablePair<>(it1.next(), it2.next());
      }
    };
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(it3, Spliterator.NONNULL | Spliterator.ORDERED), false);
  }
  public static <A> Stream<A> fromIterator(Iterator<A> xs) {
    return StreamSupport.stream(
      Spliterators.spliteratorUnknownSize(xs, Spliterator.NONNULL | Spliterator.ORDERED), false);
  }
}
