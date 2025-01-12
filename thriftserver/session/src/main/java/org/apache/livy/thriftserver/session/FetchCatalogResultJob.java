/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.livy.thriftserver.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.livy.Job;
import org.apache.livy.JobContext;

public class FetchCatalogResultJob implements Job<List<Object[]>> {
  private final String sessionId;
  private final String jobId;
  private final int maxRows;

  public FetchCatalogResultJob(String sessionId, String jobId, int maxRows) {
    this.sessionId = sessionId;
    this.jobId = jobId;
    this.maxRows = maxRows;
  }

  @Override
  public List<Object[]> call(JobContext jc) throws Exception {
    ThriftSessionState session = ThriftSessionState.get(jc, sessionId);
    Iterator<Object[]> iterator = session.findCatalogJob(jobId).iter;

    List<Object[]> result = new ArrayList<>();
    int n = 0;
    while (iterator.hasNext() && n < maxRows) {
      result.add(iterator.next());
      n += 1;
    }
    return result;
  }
}
