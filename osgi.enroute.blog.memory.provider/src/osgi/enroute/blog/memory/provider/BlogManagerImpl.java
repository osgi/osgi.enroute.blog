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

package osgi.enroute.blog.memory.provider;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import osgi.enroute.blog.api.BlogManager;
import osgi.enroute.blog.api.domain.BlogPost;
import aQute.bnd.annotation.component.Component;

/**
 * An implementation of the {@link BlogManager} API. This implementation keeps
 * the blogs inside a map, so all contents is lost when you restart.
 */
@Component(immediate = true)
public class BlogManagerImpl implements BlogManager {
	final Map<Long, BlogPost>	posts	= new ConcurrentHashMap<Long, BlogPost>();
	final AtomicLong			ids		= new AtomicLong(1000);

	@Override
	public long createPost(BlogPost post) throws Exception {
		BlogPost newPost = new BlogPost();
		newPost.id = ids.incrementAndGet();
		newPost.created = System.currentTimeMillis();
		posts.put(newPost.id, newPost);

		update(post, newPost);
		return newPost.id;
	}

	private void update(BlogPost original, BlogPost toBeUpdate) {
		toBeUpdate.content = original.content;
		toBeUpdate.title = original.title;
		toBeUpdate.lastModified = System.currentTimeMillis();
	}

	@Override
	public void updatePost(BlogPost post) throws Exception {
		BlogPost p = posts.get(post.id);
		if (p == null)
			throw new FileNotFoundException("No such post " + post.id);

		update(post, p);
	}

	@Override
	public BlogPost getPost(long id) throws Exception {
		return posts.get(id);
	}

	@Override
	public Iterable<BlogPost> queryBlogs(String query)
			throws Exception {
		if (query == null) {
			return posts.values();
		}
		// Very simplistic way to support globbing
		query = query.trim();
		query = query.replace("*", ".*").replace("?", ".?");
		query = "(" + query.replaceAll("[\\s+]+ ", ")|(") + ")";

		Pattern q = Pattern.compile(query, Pattern.CASE_INSENSITIVE);

		List<BlogPost> result = new ArrayList<BlogPost>();

		for (BlogPost p : posts.values()) {
			if (q.matcher(p.content).find() || q.matcher(p.title).find())
				result.add(p);
		}
		return result;
	}

	@Override
	public void deletePost(long id) throws Exception {
		posts.remove(id);
	}
}
