/*
 *
 * Copyright 2018 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.ampt2d.commons.accession.persistence.history.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.ampt2d.commons.accession.core.AccessionStatus;
import uk.ac.ebi.ampt2d.test.configuration.TestLongAccessionHistoryConfiguration;
import uk.ac.ebi.ampt2d.test.persistence.TestAccessionHistoryLongEntity;
import uk.ac.ebi.ampt2d.test.persistence.TestAccessionHistoryLongRepository;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = TestLongAccessionHistoryConfiguration.class)
public class BasicAccessionHistoryTrackingServiceTestForAccessionLongType {

    @Autowired
    private TestAccessionHistoryLongRepository longAccessionRepository;

    @Autowired
    private BasicAccessionHistoryTrackingService<TestAccessionHistoryLongEntity, Long>
            historyTrackingServiceForLongEntity;

    @Test
    public void merge() throws Exception {
        historyTrackingServiceForLongEntity.merge(Arrays.asList(1L, 2L, 3L),
                "MergeReason");

        assertEquals(3, longAccessionRepository.findAllByAccessionStatus(AccessionStatus.MERGED).size());
    }

    @Test
    public void update() throws Exception {
        historyTrackingServiceForLongEntity.update(Arrays.asList(1L, 2L, 3L),
                "UpdateReason");

        assertEquals(3, longAccessionRepository.findAllByAccessionStatus(AccessionStatus.UPDATED).size());
    }

    @Test
    public void deprecate() throws Exception {
        historyTrackingServiceForLongEntity.deprecate(Arrays.asList(1L, 2L, 3L),
                "DeprecateReason");

        assertEquals(3, longAccessionRepository.findAllByAccessionStatus(AccessionStatus.DEPRECATED).size());
    }
}