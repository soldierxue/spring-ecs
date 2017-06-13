/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.petclinic.vet;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.samples.petclinic.model2.*;
import org.springframework.samples.petclinic.vet.*;

@RestController
class VetController {
    @Inject
    private VetRepository vets;
    private static final Logger logger = LoggerFactory.getLogger(VetController.class);
    
    @RequestMapping(value = "/vets", method = RequestMethod.GET)
    public ResponseEntity<List<Vet>> getVets(){
        Iterable<Vet> vetIter = this.vets.findAll();
        List<Vet> vetList = new ArrayList<Vet>();
        vetIter.forEach(vet -> {
            logger.info(vet.toString());
            vetList.add(vet);
        });
        return new ResponseEntity<List<Vet>>(vetList, HttpStatus.OK);
    }
}
