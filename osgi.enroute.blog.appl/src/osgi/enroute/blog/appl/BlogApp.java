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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import aQute.bnd.annotation.component.Component;
import aQute.service.rest.Options;
import aQute.service.rest.ResourceManager;

/**
 * This is the enRoute Blog App's facade for the Javascript code. It provides a
 * REST end point. See {@link ResourceManager}.
 * <p/>
 * For this application the facade looks rather thin, basically duplicating the
 * {@link BlogManager} API. However, in real world situation this will be your
 * entry point from the external world. This will require user management
 * handling, security, and often combining multiple services. It it is therefore
 * bests to plan ahead and always use a facade for the Javascript code.
 */
@Component
public class BlogApp implements ResourceManager {
	final Map<Long, BlogPost>	posts	= new ConcurrentHashMap<Long, BlogPost>();
	final AtomicLong			ids		= new AtomicLong(1000);

	/*
	 * The Options interface provides access to the Http Servlet
	 * Request/Response objects. Additional methods on this interface are
	 * treated as parameters on the URL. I.e. {@code
	 * http://example.com/rest/blogpost?x=4} would be accessible as {@code int
	 * x()} on the interface
	 */
	interface BlogPostOptions extends Options {
		/*
		 * The query parameter, can be {@code null} or empty for all posts.
		 */
		String query();

		/*
		 * Specifies the body of the request
		 */
		BlogPost _();
	}

	/*
	 * Maps to {@code GET /rest/blogpost}. If the query is not set ({@code null}
	 * or empty), we return all blog posts.
	 */
	public Iterable<BlogPost> getBlogpost(BlogPostOptions options)
			throws Exception {
		String query = options.query();
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

	/*
	 * Create a new blog post. Maps to {@code POST /rest/blogpost} with a body
	 * that contains a Blog Post.
	 */
	public BlogPost postBlogpost(BlogPostOptions opts) throws Exception {
		BlogPost post = opts._();
		post.id = ids.incrementAndGet();
		post.created = System.currentTimeMillis();
		post.lastModified = System.currentTimeMillis();
		posts.put(post.id, post);
		return post;
	}

	/*
	 * Update an existing blog post. Maps to {@code POST /rest/blogpost/<id>}
	 * with a body that contains a Blog Post.
	 */
	public BlogPost postBlogpost(BlogPostOptions opts, long id) throws Exception {
		BlogPost post = opts._();
		BlogPost old = posts.get(id);
		old.content = post.content;
		old.title = post.title;
		post.lastModified = System.currentTimeMillis();
		return old;
	}

	/*
	 * Delete an existing blog post. Maps to {@code DELETE /rest/blogpost/<id>}
	 * without a body.
	 */
	public void deleteBlogpost(Options opts, long id) throws Exception {
		posts.remove(id);
	}

	/*
	 * Get an existing blog post. Maps to {@code GET /rest/blogpost/<id>}.
	 */
	public BlogPost getBlogpost(Options opts, long id) throws Exception {
		return posts.get(id);
	}

	/*
	 * REST interfaces are not that nice for a lot of small commands since these
	 * commands do not map very wel to the restricted set of verbs on objects.
	 * We therefore create an enum with the commands and use a {@code command}
	 * parameter on the url.
	 */
	enum Command {
		/*
		 * This method creates a number of blogs with the beginnings of chapters
		 * of the 'Alice in Wonderland' book by Lewis Caroll.
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
	public void getCommand(Options opts, Command command) throws Exception {
		switch (command) {
			case TESTDATA :
				testdata();
				break;
			case EXCEPTION :
				throw new Exception("Yuck!");
			default :
				break;

		}

	}

	/*
	 * Create a number of Blog Posts with stories from Alice in Wonderland
	 */

	void testdata() throws Exception {
		try {
			post(
					"Down the Rabbit Hole",
					"Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing "
							+ "to do: once or twice she had peeped into the book her sister was reading, but it had no pictures "
							+ "or conversations in it, `and what is the use of a book,' thought Alice `without pictures "
							+ "or conversation?' So she was considering in her own mind (as well as she could, for the "
							+ "hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain "
							+ "would be worth the trouble of getting up and picking the daisies, when suddenly a White Rabbit "
							+ "with pink eyes ran close by her.There was nothing so VERY remarkable in that; nor did Alice think "
							+ "it so VERY much out of the way to hear the Rabbit say to itself, `Oh dear! Oh dear! I shall be late!' "
							+ "(when she thought it over afterwards, it occurred to her that she ought to have wondered at this, "
							+ "but at the time it all seemed quite natural); but when the Rabbit actually TOOK A WATCH OUT "
							+ "OF ITS WAISTCOAT- POCKET, and looked at it, and then hurried on, Alice started to her feet, "
							+ "for it flashed across her mind that she had never before see a rabbit with either a "
							+ "waistcoat-pocket, or a watch to take out of it, and burning with curiosity, she ran across "
							+ "the field after it, and fortunately was just in time to see it pop down a large rabbit-hole "
							+ "under the hedge. In another moment down went Alice after it, never once considering how in "
							+ "the world she was to get out again. The rabbit-hole went straight on like a tunnel for some way, "
							+ "and then dipped suddenly down, so suddenly that Alice had not a moment to think about stopping "
							+ "herself before she found herself falling down a very deep well. Either the well was very deep, "
							+ "or she fell very slowly, for she had plenty of time as she went down to look about her and to "
							+ "wonder what was going to happen next. First, she tried to look down and make out what she was "
							+ "coming to, but it was too dark to see anything; then she looked at the sides of the well, "
							+ "and noticed that they were filled with cupboards and book-shelves; here and there she saw maps "
							+ "and pictures hung upon pegs. She took down a jar from one of the shelves as she passed; it "
							+ "was labelled `ORANGE MARMALADE', but to her great disappointment it way empty: she did not "
							+ "like to drop the jar for fear of killing somebody, so managed to put it into one of the cupboards "
							+ "as she fell past it. `Well!' thought Alice to herself, `after such a fall as this, I shall "
							+ "think nothing of tumbling down stairs! How brave they'll all think me at home! Why, "
							+ "I wouldn't say anything about it, even if I fell off the top of the house!' (Which was very "
							+ "likely true.) Down, down, down. Would the fall NEVER come to an end! `I wonder how many "
							+ "miles I've fallen by this time?' she said aloud. `I must be getting somewhere near the "
							+ "centre of the earth. Let me see: that would be four thousand miles down, I think--' (for, you "
							+ "see, Alice had learnt several things of this sort in her lessons in the schoolroom, and though this "
							+ "was not a VERY good opportunity for showing off her knowledge, as there was no one to listen to her, "
							+ "still it was good practice to say it over) `--yes, that's about the right distance--but then I "
							+ "wonder what Latitude or Longitude I've got to?' (Alice had no idea what Latitude was, or Longitude "
							+ "either, but thought they were nice grand words to say.) Presently she began again. `I wonder if I "
							+ "shall fall right THROUGH the earth! How funny it'll seem to come out among the people that walk with "
							+ "their heads downward! The Antipathies, I think--' (she was rather glad there WAS no one listening, "
							+ "this time, as it didn't sound at all the right word) `--but I shall have to ask them what the name "
							+ "of the country is, you know. Please, Ma'am, is this New Zealand or Australia?' (and she tried to "
							+ "curtsey as she spoke--fancy CURTSEYING as you're falling through the air! Do you think you could "
							+ "manage it?) `And what an ignorant little girl she'll think me for asking! No, it'll never do to "
							+ "ask: perhaps I shall see it written up somewhere.' Down, down, down. There was nothing else "
							+ "to do, so Alice soon began talking again. Dinah'll miss me very much to-night, I should think!' "
							+ "(Dinah was the cat.) `I hope they'll remember her saucer of milk at tea-time. Dinah my dear! I "
							+ "wish you were down here with me! There are no mice in the air, I'm afraid, but you might catch "
							+ "a bat, and that's very like a mouse, you know. But do cats eat bats, I wonder?' And here Alice "
							+ "began to get rather sleepy, and went on saying to herself, in a dreamy sort of way, `Do cats eat "
							+ "bats? Do cats eat bats?' and sometimes, `Do bats eat cats?' for, you see, as she couldn't answer "
							+ "either question, it didn't much matter which way she put it. She felt that she was dozing off, and "
							+ "had just begun to dream that she was walking hand in hand with Dinah, and saying to her very earnestly, "
							+ "`Now, Dinah, tell me the truth: did you ever eat a bat?' when suddenly, thump! thump! down she came upon "
							+ "a heap of sticks and dry leaves, and the fall was over. Alice was not a bit hurt, and she jumped up on to "
							+ "her feet in a moment: she looked up, but it was all dark overhead; before her was another long passage, and "
							+ "the White Rabbit was still in sight, hurrying down it. There was not a moment to be lost: away "
							+ "went Alice like the wind, and was just in time to hear it say, as it turned a corner, `Oh my "
							+ "ears and whiskers, how late it's getting!' She was close behind it when she turned to corner, "
							+ "but the Rabbit was no longer to be seen: she found herself in a long, low hall, which was lit "
							+ "up by a row of lamps hanging from the roof.");
			post(
					"The Pool of Tears",
					"`Curiouser and curiouser!' cried Alice (she was so much surprised, that for the moment "
							+ "she quite forgot how to speak good English); `now I'm opening out like the largest "
							+ "telescope that ever was! Good-bye, feet!' (for when she looked down at her feet, "
							+ "they seemed to be almost out of sight, they were getting so far off). `Oh, my poor "
							+ "little feet, I wonder who will put on your shoes and stockings for you now, dears? "
							+ "I'm sure _I_ shan't be able! I shall be a great deal too far off to trouble myself "
							+ "about you: you must manage the best way you can; --but I must be kind to them,' thought "
							+ "Alice, `or perhaps they won't walk the way I want to go! Let me see: I'll give them a "
							+ "new pair of boots every Christmas.' And she went on planning to herself how she would "
							+ "manage it. `They must go by the carrier,' she thought; `and how funny it'll seem, sending "
							+ "presents to one's own feet! And how odd the directions will look!\n"
							+ "            ALICE'S RIGHT FOOT, ESQ.\n"
							+ "                HEARTHRUG,\n"
							+ "                    NEAR THE FENDER,\n"
							+ "                        (WITH ALICE'S LOVE).\n"
							+ "Oh dear, what nonsense I'm talking!'\n"
							+ "Just then her head struck against the roof of the hall: in fact she was now more than nine feet "
							+ "high, and she at once took up the little golden key and hurried off to the garden door.");
			post(
					"A Caucus Race and a Long Tail",
					"They were indeed a queer-looking party that assembled on the bank--the birds with draggled feathers, the "
							+ "animals with their fur clinging close to them, and all dripping wet, cross, and uncomfortable. The first "
							+ "question of course was, how to get dry again: they had a consultation about this, and after a few minutes "
							+ "it seemed quite natural to Alice to find herself talking familiarly with them, as if she had known them all "
							+ "her life. Indeed, she had quite a long argument with the Lory, who at last turned sulky, and would only "
							+ "say, `I am older than you, and must know better'; and this Alice would not allow without knowing how old "
							+ "it was, and, as the Lory positively refused to tell its age, there was no more to be said. At last the "
							+ "Mouse, who seemed to be a person of authority among them, called out, `Sit down, all of you, and listen "
							+ "to me! I'LL soon make you dry enough!' They all sat down at once, in a large ring, with the Mouse in the "
							+ "middle. Alice kept her eyes anxiously fixed on it, for she felt sure she would catch a bad cold if she did "
							+ "not get dry very soon.");
			post(
					"The Rabbit Sends in a Little Bill",
					"It was the White Rabbit, trotting slowly back again, and looking anxiously "
							+ "about as it went, as if it had lost something; and she heard it muttering to itself "
							+ "`The Duchess! The Duchess! Oh my dear paws! Oh my fur and whiskers! She'll get me "
							+ "executed, as sure as ferrets are ferrets! Where CAN I have dropped them, I wonder?' "
							+ "Alice guessed in a moment that it was looking for the fan and the pair of white kid "
							+ "gloves, and she very good-naturedly began hunting about for them, but they were "
							+ "nowhere to be seen--everything seemed to have changed since her swim in the pool, "
							+ "and the great hall, with the glass table and the little door, had vanished completely.\n"
							+ "Very soon the Rabbit noticed Alice, as she went hunting about, and called out to her "
							+ "in an angry tone, `Why, Mary Ann, what ARE you doing out here? Run home this moment, and "
							+ "fetch me a pair of gloves and a fan! Quick, now!' And Alice was so much frightened that she "
							+ "ran off at once in the direction it pointed to, without trying to explain the mistake it had made.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Utility to make a BlogPost out of a title and a content.
	 */
	private BlogPost post(String title, String content) {
		BlogPost p = new BlogPost();
		p.title = title;
		p.content = content;
		p.id = ids.getAndIncrement();
		p.lastModified = p.created = System.currentTimeMillis();
		posts.put(p.id, p);
		return p;
	}
}
