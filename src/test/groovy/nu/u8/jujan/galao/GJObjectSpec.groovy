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
package nu.u8.jujan.galao

import spock.lang.Specification

import java.util.stream.Stream;

class GJObjectSpec extends Specification {
  def "extending GJObject"() {
    when:
    def e1 = new GJObject.Extension(Stream.of("test", "test2", "Aa", "BB"))
    def e2 = new GJObject.Extension(Stream.of("test", "test3"))
    def o1 = e1.extend(null, Stream.of(new GJInteger(1), new GJText("2"), new GJInteger(3),
        new GJInteger(4)))
    def o2 = e2.extend(o1, Stream.of(new GJText("5"), new GJText("6")))
    def test = new GJObject.Resolution("test")
    def test2 = new GJObject.Resolution("test2")
    def Aa = new GJObject.Resolution("Aa")
    def BB = new GJObject.Resolution("BB")
    def test3 = new GJObject.Resolution("test3")
    then:
    assert(test.resolve(o1) == new GJInteger(1))
    assert(test.resolve(o2) == new GJText("5"))
    assert(test.resolve(o1) == new GJInteger(1))
    assert(test.resolve(o2) == new GJText("5"))
    assert(test2.resolve(o1) == new GJText("2"))
    assert(test2.resolve(o1) == new GJText("2"))
    assert(test2.resolve(o2) == new GJText("2"))
    assert(test2.resolve(o2) == new GJText("2"))
    assert(Aa.resolve(o1) == new GJInteger(3))
    assert(Aa.resolve(o1) == new GJInteger(3))
    assert(Aa.resolve(o2) == new GJInteger(3))
    assert(Aa.resolve(o2) == new GJInteger(3))
    assert(BB.resolve(o1) == new GJInteger(4))
    assert(BB.resolve(o1) == new GJInteger(4))
    assert(BB.resolve(o2) == new GJInteger(4))
    assert(BB.resolve(o2) == new GJInteger(4))
    assert(test3.resolve(o2) == new GJText("6"))
    assert(test3.resolve(o2) == new GJText("6"))
  }
}
