/*
 *
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
 *
 */

package uk.ac.ebi.ampt2d.accession.generator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.ampt2d.accession.AccessionGenerator;

@Configuration
public class TestsGeneratorConfiguration {

    @Bean
    @ConditionalOnProperty(name = "test.generator", havingValue = "generatorA")
    AccessionGenerator<String> testAccessionGeneratorA() {
        return new TestAccessionGeneratorA();
    }

    @Bean
    @ConditionalOnProperty(name = "test.generator", havingValue = "generatorB")
    AccessionGenerator<String> testAccessionGeneratorB() {
        return new TestAccessionGeneratorB();
    }
}
