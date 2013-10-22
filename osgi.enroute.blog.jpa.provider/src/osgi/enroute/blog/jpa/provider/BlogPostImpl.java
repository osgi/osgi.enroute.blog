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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import osgi.enroute.blog.api.domain.BlogPost;

/**
 * The JPA persistent object. This class gets woven and handles all the JPA
 * magic under the hood. Necessary because some JPA implementations do not
 * support just public fields and the spec is not very clear in this area.
 */
@Entity
@Table(name = "POST")
@SequenceGenerator(name = "blogPostIds", initialValue = 1000, allocationSize = 100)
public class BlogPostImpl {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogPostIds")
	private long	id;
	private long	created;
	private long	lastModified;
	private String	title;

	@Column(length = 100000)
	private String	content;

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	BlogPost getBlogPost() {
		BlogPost post = new BlogPost();
		post.id = getId();
		post.content = getContent();
		post.lastModified = getLastModified();
		post.created = getCreated();
		post.title = getTitle();
		return post;
	}

}
