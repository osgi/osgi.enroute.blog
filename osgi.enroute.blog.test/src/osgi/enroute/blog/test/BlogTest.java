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

package osgi.enroute.blog.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.Iterator;
import javax.transaction.TransactionManager;
import junit.framework.TestCase;
import org.osgi.framework.FrameworkUtil;
import osgi.enroute.blog.api.BlogManager;
import osgi.enroute.blog.api.domain.BlogPost;
import aQute.bnd.annotation.component.Reference;
import aQute.test.dummy.ds.DummyDS;
import aQute.test.dummy.log.DummyLog;

/**
 * An OSGi JUnit 3.8 Test Case for the Blog Manager API
 */
public class BlogTest extends TestCase {
	BlogManager					blog;
	private TransactionManager	tm;

	/**
	 * Setup this test case using {@link DummyDS}.
	 */
	@Override
	public void setUp() throws InterruptedException {
		try {
			DummyDS ds = new DummyDS();
			ds.setContext(FrameworkUtil.getBundle(BlogTest.class)
					.getBundleContext());
			ds.add(this);
			ds.add(new DummyLog());
			ds.wire();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(1000000);
		}
	}

	/**
	 * Test the basic CRUD operations
	 */
	public void testCrud() throws Exception {

		long now = System.currentTimeMillis();

		//
		// Create a blog
		//

		tm.begin();
		long id;
		try {
			//
			// Create a post
			//
			BlogPost p = new BlogPost();
			p.title = "Title";
			p.content = "text";
			id = blog.createPost(p);
			tm.commit();
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			fail();
			return;
		}

		//
		// Get the blog we just created
		//

		tm.begin();
		try {
			BlogPost post = blog.getPost(id);
			assertNotNull(post);

			assertThat(post.content, is("text"));
			assertThat(post.title, is("Title"));
			assertTrue(post.created >= now);
			assertTrue(post.created <= System.currentTimeMillis());
			assertTrue(post.lastModified >= now);
			assertTrue(post.lastModified <= System.currentTimeMillis());

			long created = post.created;

			//
			// Update it
			//
			BlogPost p = new BlogPost();
			p.id = id;
			p.content = "other";
			p.title = "OtherTitle";
			blog.updatePost(p);
			post = blog.getPost(id);
			assertNotNull(post);

			assertThat(post.content, is("other"));
			assertThat(post.title, is("OtherTitle"));
			assertThat(post.created, is(created));
			assertTrue(post.lastModified <= System.currentTimeMillis());
			assertTrue(post.lastModified >= created);
			tm.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tm.rollback();
			fail();
		}

		/**
		 * See if we can get the blog we just created from the query
		 */
		tm.begin();
		try {
			int n = 0;
			Iterator<BlogPost> i = blog.queryBlogs(null).iterator();
			while (i.hasNext()) {
				i.next();
				n++;
			}
			assertEquals(1, n);
		} catch (Exception e) {
			tm.rollback();
			e.printStackTrace();
			fail();
		}
	}

	@Reference
	void setBlogManager(BlogManager bm) {
		this.blog = bm;
	}

	@Reference
	void setTransactionManager(TransactionManager tm) {
		this.tm = tm;
	}
}
