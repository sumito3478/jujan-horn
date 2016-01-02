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
import fj.data.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GJActorGroup {
  BlockingQueue<GJEnvelope> queue = new LinkedBlockingQueue<>();
  @NonFinal
  volatile List<GJActor> actors = List.nil();
  public synchronized void register(GJActor actor) {
    getThreads();
    actors = List.cons(actor, actors);
  }
  public void send(GJEnvelope envelope) {
    if (queue.size() == 0) {
      handle(envelope);
      return;
    }
    queue.add(envelope);
  }
  private void handle(GJEnvelope envelope) {
    // TODO
  }
  public void handle() throws InterruptedException {
    while (true) {
      actors.forEach(GJActor::tryHandleEnvelope);
      Thread.sleep(1);
    }
  }
  @Getter(lazy = true)
  private final static GJActorGroup central = new GJActorGroup();
  //  @Getter(value = AccessLevel.PRIVATE, lazy = true)
//  private final static Executor pool =
//      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  private static class HandlerThread extends Thread {
    @Override
    public void run() {
      try {
        while (true) {
          getCentral().handle();
        }
      } catch (InterruptedException e) {
        throw new InternalError(e);
      }
    }
    public static HandlerThread create() {
      val ret = new HandlerThread();
      ret.start();
      return ret;
    }
  }
  @Getter(value = AccessLevel.PRIVATE, lazy = true)
  private final static List<HandlerThread> threads = IntStream
      .range(0, Runtime.getRuntime().availableProcessors() + 1)
      .mapToObj(i -> HandlerThread.create())
      .collect(fj.data.Collectors.toList());

}
