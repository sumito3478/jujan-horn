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

package nu.u8.jujan.kafei.internal

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.BailErrorStrategy
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import spock.lang.Specification;

class JujanParserSpec extends Specification {
  private def parseModule(src) {
    def ins = new ANTLRInputStream(src)
    def lexer = new JujanLexer(ins)
    def tokens = new CommonTokenStream(lexer)
    def parser = new JujanParser(tokens)
    parser.setErrorHandler(new BailErrorStrategy())
    def module = parser.module()
    def visitor = TreeGeneratingANTLR4Visitor.of("test.jujan", ImmutableTree.factory)
    return visitor.visitModule(module);
  }

  def "parse succeeds with valid inputs"() {
    when:
    parseModule(src)
    then:
    noExceptionThrown()
    where:
    src                                                     || result
    ""                                                      || true
    "#import IO"                                            || true
    "1000;"                                                 || true
    "1000 + 1000;"                                          || true
    "IO::println;"                                          || true
    "IO::println(\"test!\");"                               || true
    "#import IO\nIO.println;"                               || true
    "#import IO\nIO.println(\"test!\");"                    || true
    "1000;"                                                 || true
    "1000 + 1000;"                                          || true
    "def test = () -> \"test\";"                            || true
    "def test = () -> do { IO::println(1); 1 };"            || true
    "def test = () -> do { let a = 1; IO::println(a); a };" || true
    "def test = (a : A = x, b) -> do { let a = 1; IO::println(a); a };" || true
  }

  def "parse fails with invalid inputs"() {
    when:
    parseModule(src)
    then:
    thrown(ParseCancellationException)
    where:
    src                                 || result
    "def -> -> -> this should be error" || false
  }
}
