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

package osgi.enroute.blog.appl;

import aQute.bnd.annotation.component.Component;
import aQute.service.rest.Options;
import aQute.service.rest.ResourceManager;

/**
 * This is the enRoute Blog App's facade for the Javascript code. It provides a
 * REST end point. See {@link ResourceManager}.
 */
@Component
public class BlogApp implements ResourceManager {
	/*
	 * REST interfaces are not that nice for a lot of small commands since these
	 * commands do not map very wel to the restricted set of verbs on objects.
	 * We therefore create an enum with the commands and use a {@code command}
	 * parameter on the url.
	 */
	enum Command {
		/*
		 * Return the time
		 */
		TESTDATA,
		/*
		 * This method throws an exception for testing.
		 */
		EXCEPTION
	}

	/*
	 * This method executes a generic command. Maps to the {@code /rest/command/
	 * 
	 * @param opts
	 * 
	 * @throws Exception
	 */

	public static class BlogPost {
		BlogPost(String title, String content) {
			this.title = title;
			this.content = content;
		}

		public String	content;
		public String	title;
	}

	public Object getCommand(Options opts, Command command) throws Exception {
		switch (command) {
			case TESTDATA :
				BlogPost bp = new BlogPost(
						"A Mad Tea Party",
						"'You can't think how glad I am to see you again, you dear old thing!' said the Duchess, as she tucked her arm affectionately into Alice's, and they walked off together.\n"
								+ "Alice was very glad to find her in such a pleasant temper, and thought to herself that perhaps it was only the pepper that had made her so savage when they met in the kitchen.\n"
								+ "'When I'M a Duchess,' she said to herself, (not in a very hopeful tone though), 'I won't have any pepper in my kitchen AT ALL. Soup does very well without—Maybe it's always pepper that makes people hot-tempered,' she went on, very much pleased at having found out a new kind of rule, 'and vinegar that makes them sour—and camomile that makes them bitter—and—and barley-sugar and such things that make children sweet-tempered. I only wish people knew that: then they wouldn't be so stingy about it, you know—'\n"
								+ "She had quite forgotten the Duchess by this time, and was a little startled when she heard her voice close to her ear. 'You're thinking about something, my dear, and that makes you forget to talk. I can't tell you just now what the moral of that is, but I shall remember it in a bit.'\n");
				return bp;

			case EXCEPTION :
				throw new Exception("Yuck!");
			default :
				break;

		}
		return null;
	}
}
