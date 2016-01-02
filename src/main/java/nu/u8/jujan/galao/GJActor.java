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

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(callSuper = true)
public class GJActor extends GJObject {
  GJObject module;
  @Getter(lazy = true)
  private final Map<String, GJObject> slots = module.toMutableMap();
  ConcurrentLinkedQueue<GJEnvelope> mailbox = new ConcurrentLinkedQueue<>();
  ReentrantLock lock = new ReentrantLock();
  public GJActor() {
    GJActorGroup.getCentral().register(this);
  }
  boolean tryHandleEnvelope() {
    if (lock.tryLock()) {
      val e = mailbox.poll();
      if (e != null) {
        handleEnvelope(e);
        return true;
      }
    }
    return false;
  }
  private static Resolution receive = new Resolution("[[Receive]]");
  private static Extension message = new Extension(Stream.of("message"));
  private void handleEnvelope(GJEnvelope envelope) {
    if (envelope.getMessage() instanceof GJCall) {
      val f = (GJFunction)getSlots().get(((GJCall) envelope.getMessage()).getCallee().getName());
      val ret = f.apply(GJObject.empty, ((GJCall) envelope.getMessage()).getArguments());
      val cont = ((GJCall) envelope.getMessage()).getContinuation();
      if (cont != null)
        envelope.getSender().send(new GJEnvelope(this, envelope.getSender(), cont));
      return;
    }
    ((GJFunction)receive.resolve(module)).apply(GJObject.empty,
        message.extend(null, Stream.of(envelope.getMessage())));
  }
  void send(GJEnvelope envelope) {
    mailbox.add(envelope);
  }
}
