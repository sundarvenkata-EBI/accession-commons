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
package uk.ac.ebi.ampt2d.commons.accession.persistence.history.entity;

import uk.ac.ebi.ampt2d.commons.accession.core.AccessionStatus;

import javax.persistence.Column;

public abstract class AccessionHistoryStringEntity extends AccessionHistoryEntity {

    @Column(nullable = false)
    private String accession;

    public AccessionHistoryStringEntity(String accession, AccessionStatus accessionStatus, String reason) {
        super(accessionStatus, reason);
        this.accession = accession;
    }
}
