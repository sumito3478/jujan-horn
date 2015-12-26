package nu.u8.jujan.horn.internal;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
public class StreamUtil {
  public static <A, B> Stream<Pair<A, B>> zip(Stream<A> xs, Stream<B> ys) {
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
}
