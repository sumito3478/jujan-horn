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

package nu.u8.jujan.kafei.internal;

import lombok.val;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeUtil {
  private final static java.util.List<Class<?>> treeInterfaces =
      Arrays.stream(Tree.class.getDeclaredClasses())
          .filter(Class::isInterface)
          .filter(Tree.class::isAssignableFrom)
          .collect(Collectors.toList());
  public static Tree.Factory createFactoryImplementation(Class<?> implementor) {
    List<Class<?>> implementors = Arrays.stream(implementor.getDeclaredClasses()).collect(Collectors.toList());
    Map<String, Class<?>> implMap = treeInterfaces.stream().collect(Collectors.toMap(
        Class::getSimpleName,
        x -> Arrays.stream(implementor.getDeclaredClasses()).filter(x::isAssignableFrom)
            .findFirst().get()
    ));
    return (Tree.Factory) Proxy.newProxyInstance(
        TreeUtil.class.getClassLoader(),
        new Class[]{Tree.Factory.class},
        new InvocationHandler() {
          private final Object dummy = new Object();
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            val name = method.getName();
            if (!name.startsWith("new"))
              return method.invoke(dummy, args);
            val className = name.substring(3);
            val c = implMap.get(className);
            val m = Arrays.asList(c.getDeclaredMethods()).stream().filter(x -> x.getName().equals
                ("of"))
                .findFirst().get();
            return m.invoke(null, args);
          }
        }
    );
  }
}
