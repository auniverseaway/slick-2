/*
 * Copyright 2016 Chris Millar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.millr.slick.services;

import java.util.Iterator;
import java.util.Map;

import javax.jcr.NodeIterator;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;

public interface CommentService {

    public Iterator<Resource> getComments(Resource item);
    
    public NodeIterator getComments(Session session, Long offset, Long limit, String status);
    
    public NodeIterator getItemPublicComments(Resource item, String status);
    
    public long getTotalComments(Session session, String status, Long pageSize);
    
    public Long getNumberOfComments(Session session, String status);

    public Resource createComment(Resource item, Map<String,Object> properties);
	
}