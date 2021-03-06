/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jooby.sample;

import org.jooby.Jooby;
import org.jooby.json.Gzon;
import org.jooby.sample.entity.Person;
import org.jooby.sample.json.LocalDateAsJson;
import org.jooby.sample.mvc.RestController;

import java.time.LocalDate;

import static org.jooby.MediaType.form;
import static org.jooby.MediaType.json;

public final class RestApiMain extends Jooby {
  {
    use(new Gzon().doWith((builder, config) ->
      builder.registerTypeAdapter(LocalDate.class, LocalDateAsJson.withDefault())
    ));

    use(RestController.class);

    use("/script/rest")
      .get(request -> Person.LUKE)
      .post(request -> new Person(
        request.param("firstName").value(),
        request.param("lastName").value(),
        request.param("birthDate").toOptional(LocalDate.class).orElse(LocalDate.now())
      ))
//      .consumes("application/x-www-form-urlencoded")
      .consumes(form)
      .produces(json);
  }

  public static void main(String... args) {
    run(RestApiMain::new, args);
  }
}
