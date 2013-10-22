/*
 * Copyright (c) OSGi Alliance (2013). All Rights Reserved.
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

package osgi.enroute.blog.api.domain;

import osgi.enroute.blog.api.BlogManager;

/**
 * A Blog Post contains the information for a specific entry in a blog. It is
 * associated with an author, it has content, and it maintains the time it was
 * created and modified.
 */
public class BlogPost {
	/**
	 * The primary key for this blog. It is assigned by the {@link BlogManager}
	 */
	public long		id;

	/**
	 * The title of the post
	 */
	public String	title;

	/**
	 * The content for this blog.
	 */
	public String	content;

	/**
	 * The system for the blog creation.
	 */
	public long		created;

	/**
	 * The last modified time.
	 */
	public long		lastModified;
}
