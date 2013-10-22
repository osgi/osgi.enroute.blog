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

package osgi.enroute.blog.api;

import osgi.enroute.blog.api.domain.BlogPost;
import aQute.bnd.annotation.ProviderType;

/**
 * A Manager of blogs. Provides create, read, update, list and delete methods
 * for {@link BlogPost} domain objects.
 */
@ProviderType
public interface BlogManager {
	/**
	 * Create a post for a user with a content. Must set the
	 * {@link BlogPost#lastModified} and {@link BlogPost#created} times.
	 * 
	 * @param post The content (must not be {@code null})
	 * @return the new id for this blog post
	 * @throws Exception
	 */
	long createPost(BlogPost post) throws Exception;

	/**
	 * Update a an existing post with a new text.
	 * 
	 * @param id The id of the blog post (as assigned by the system in
	 *        {@link #createPost(BlogPost)}
	 * @param post The content (must not be {@code null})
	 * @throws Exception
	 */
	void updatePost(BlogPost post) throws Exception;

	/**
	 * Get a single post.
	 * 
	 * @param id The id of the blog post (as assigned by the system in
	 *        {@link #createPost(BlogPost)}
	 * @return the Blog Post associated with the id
	 * @throws Exception
	 */
	BlogPost getPost(long id) throws Exception;

	/**
	 * Find a set of posts by a query. The query is broken up in words and
	 * matched against the contents of the {@link BlogPost} objects.
	 * Implementations may provide an internal syntax to provide more user
	 * oriented features. E.g. a - for negation.
	 * 
	 * @param query The query
	 * @return A list of {@link BlogPost} objects
	 * @throws Exception
	 */
	Iterable<BlogPost> queryBlogs(String query) throws Exception;

	/**
	 * Delete a post.
	 * 
	 * @param id The id of the post.
	 * @throws Exception
	 */
	void deletePost(long id) throws Exception;
}
