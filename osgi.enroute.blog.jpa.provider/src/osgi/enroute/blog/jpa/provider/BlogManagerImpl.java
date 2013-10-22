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

package osgi.enroute.blog.jpa.provider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import osgi.enroute.blog.api.BlogManager;
import osgi.enroute.blog.api.domain.BlogPost;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

/**
 * This {@link BlogManager} implements the API via JPA. Since this is a simple
 * example, we implement it rather trivially.
 * <p/>
 * The Blog Manager API specifies a Blog Post as a <em>data</em> object. The
 * advantage of data objects is that they make it clear that this is PUBLIC API
 * and therefore can easily be transferred to other processes. I.e. there is
 * nothing to hide. Unfortunately, some JPA implementations cannot handle data
 * objects (the JPA spec is unclear about this) since they feel the need to
 * weave the accessor methods. This limitation forces us to maintain our own
 * domain objects (with accessors) and copy the data in/out from the
 * specification object. Though this could largely be automated using reflection
 * it is done manually.
 * <p/>
 * To use JPA, we need an {@link EntityManager}. The OSGi JPA Managed mode
 * provides us with an Entity Manager through the service registry. This EM is
 * managed, that means we never open nor close it, but can only use it on a
 * single thread during an active transaction!
 * <p/>
 * This class is a component. It does not take any configuration.
 */
@Component
@SuppressWarnings("unchecked")
public class BlogManagerImpl implements BlogManager {

	private EntityManager	em;

	@Override
	public long createPost(BlogPost content) throws Exception {
		BlogPostImpl post = new BlogPostImpl();
		post.setCreated(System.currentTimeMillis());

		update(content, post);
		return post.getId();
	}

	@Override
	public void updatePost(BlogPost content) throws Exception {
		BlogPostImpl old = getPostImpl(content.id);
		if (old == null)
			throw new FileNotFoundException("No such post");

		update(content, old);
	}

	/*
	 * Helper to update the persistent object with the domain object.
	 */
	private void update(BlogPost from, BlogPostImpl to) {
		to.setContent(from.content);
		to.setTitle(from.title);
		to.setLastModified(System.currentTimeMillis());
		em.persist(to);
		em.flush();
	}

	@Override
	public BlogPost getPost(long id) throws Exception {
		BlogPostImpl post = getPostImpl(id);
		if (post == null)
			return null;

		return post.getBlogPost();
	}

	@Override
	public Iterable<BlogPost> queryBlogs(String query)
			throws Exception {

		//
		// We should do the selection of the posts in the query
		// but that is kind of complicated so we do post filtering
		//
		List<BlogPostImpl> posts = em.createQuery(
				"SELECT p FROM BlogPostImpl p").getResultList();

		List<BlogPost> result = new ArrayList<>();
		Pattern filter = null;

		//
		// Simplistic query filter
		//

		if (query != null && !posts.isEmpty()) {
			query = query.trim();
			query = query.replace("*", ".*").replace("?", ".?");
			query = "(" + query.replaceAll("\\s+", ")|(") + ")";
			filter = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
		}

		for (BlogPostImpl p : posts) {
			if (filter == null || filter.matcher(p.getContent()).find() || filter.matcher(p.getTitle()).find())
				result.add(p.getBlogPost());
		}
		return result;
	}

	@Override
	public void deletePost(long postId) throws Exception {
		BlogPostImpl post = getPostImpl(postId);
		if (post == null)
			throw new FileNotFoundException("No such post");

		em.remove(post);
	}

	/*
	 * Helper
	 */
	private BlogPostImpl getPostImpl(long id) {
		try {
			BlogPostImpl post = (BlogPostImpl) em
					.createQuery("SELECT p FROM BlogPostImpl p WHERE p.id=:id")
					.setParameter("id", id).getSingleResult();
			return post;
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	/**
	 * Our reference to the Entity Manager
	 */
	@Reference
	void setEM(EntityManager em) {
		this.em = em;
	}
}
