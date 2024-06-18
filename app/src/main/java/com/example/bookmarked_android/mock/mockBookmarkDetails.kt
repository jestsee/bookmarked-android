package com.example.bookmarked_android.mock

import com.example.bookmarked_android.model.BookmarkDetail
import com.example.bookmarked_android.network.customTypeAdapter
import com.google.gson.reflect.TypeToken

private val mockedBookmarkDetailsData = """
    [
        {
            "id": "f6b7686b-9493-4ca4-8a32-35e713ad5ea5",
            "author": {
                "name": "fks",
                "username": "@FredKSchott",
                "avatar": "https://pbs.twimg.com/profile_images/1272979356529221632/sxvncugt_normal.jpg"
            },
            "tweetUrl": "https://x.com/FredKSchott/status/1800548938434642119",
            "parentId": "f6b7686b-9493-4ca4-8a32-35e713ad5ea5",
            "contents": [
                {
                    "id": "781bb400-63a1-428a-ac04-de15c92e104d",
                    "type": "text",
                    "text": "This now runs without JavaScript 🤯"
                },
                {
                    "id": "ed46e5be-22bb-4e05-94f4-a633b4c02a10",
                    "type": "text",
                    "text": "astro-zerojs-transitions.vercel.app",
                    "url": "https://astro-zerojs-transitions.vercel.app/"
                },
                {
                    "id": "8454b5de-93db-44f0-b8b4-d20d950b6179",
                    "type": "image",
                    "url": "https://pbs.twimg.com/ext_tw_video_thumb/1800548087854669824/pu/img/EgORVlcPr7SfN0zP.jpg"
                }
            ]
        },
        {
            "id": "2c0a4d82-56aa-4bba-ac5b-611329700a5b",
            "author": {
                "name": "fks",
                "username": "@FredKSchott",
                "avatar": "https://pbs.twimg.com/profile_images/1272979356529221632/sxvncugt_normal.jpg"
            },
            "tweetUrl": "https://x.com/FredKSchott/status/1800549266269880380",
            "parentId": "2c0a4d82-56aa-4bba-ac5b-611329700a5b",
            "contents": [
                {
                    "id": "a9746195-28fa-4415-803b-d06564b75881",
                    "type": "text",
                    "text": "Zero-JS View Transitions require Google Chrome 126 or Microsoft Edge 126 to run, both rolling out this week starting today."
                },
                {
                    "id": "7c7a6541-dcae-4d96-b1c7-d9cef70c8caa",
                    "type": "text",
                    "text": "More info here 👉"
                },
                {
                    "id": "7c7a6541-dcae-4d96-b1c7-d9cef70c8caa",
                    "type": "text",
                    "text": "x.com/astrodotbuild/…",
                    "url": "https://x.com/astrodotbuild/status/1800547817825706161"
                },
                {
                    "id": "080eb464-1b0d-46bc-aa99-269617effb50",
                    "type": "callout",
                    "author": {
                        "name": "Astro",
                        "username": "@astrodotbuild",
                        "avatar": "https://pbs.twimg.com/profile_images/1632785343433908224/SnMGR19O_normal.png"
                    },
                    "parentId": "080eb464-1b0d-46bc-aa99-269617effb50",
                    "contents": [
                        {
                            "id": "f44923fa-cc4c-4076-8658-5940452fa659",
                            "type": "text",
                            "text": "They're finally here: Zero-JS View Transitions"
                        },
                        {
                            "id": "159aa730-2ea4-4bf4-b3a5-5d13fc9fe55c",
                            "type": "text",
                            "text": "Chrome just shipped support for native, app-like animations between page navigation. Just CSS & HTML, no JS required! Try them today with built-in Astro support on day 1. "
                        },
                        {
                            "id": "1f0d97d2-24bc-418d-bcdd-73d5458d573b",
                            "type": "text",
                            "text": "astro.build/blog/future-of…",
                            "url": "https://astro.build/blog/future-of-astro-zero-js-view-transitions/?tw="
                        }
                    ]
                }
            ]
        },
        {
            "id": "4dc01f7d-394d-4ab2-a404-2aab6bd2195e",
            "author": {
                "name": "fks",
                "username": "@FredKSchott",
                "avatar": "https://pbs.twimg.com/profile_images/1272979356529221632/sxvncugt_normal.jpg"
            },
            "tweetUrl": "https://x.com/FredKSchott/status/1800652292825158112?s=35",
            "parentId": "4dc01f7d-394d-4ab2-a404-2aab6bd2195e",
            "contents": [
                {
                    "id": "1bcfcd8d-05fc-4c6b-9ec4-44a863265ed6",
                    "type": "text",
                    "text": "Matthew with a great thread on how/why we designed Astro for this future:"
                },
                {
                    "id": "fcfbea3c-2b6f-4471-a90f-c126505f0679",
                    "type": "text",
                    "text": "x.com/matthewcp/stat…",
                    "url": "https://x.com/matthewcp/status/1800584127718293545"
                },
                {
                    "id": "e45f32c3-d013-43fc-ad44-7b72cbf75f08",
                    "type": "callout",
                    "author": {
                        "name": "Matthew Phillips",
                        "username": "@matthewcp",
                        "avatar": "https://pbs.twimg.com/profile_images/1758242509652647936/JuXuCA4z_normal.jpg"
                    },
                    "parentId": "e45f32c3-d013-43fc-ad44-7b72cbf75f08",
                    "contents": [
                        {
                            "id": "55c30cea-8f96-47a3-ab26-43f127e17ceb",
                            "type": "text",
                            "text": "When we built View Transitions support into Astro we had previously explored a few different ways to do client-side routing. Then we took a step back and asked, what's wrong with MPA routing in the first place? The answer was, not much."
                        },
                        {
                            "id": "8ce3119b-2c03-4007-b113-ecf2985aeab7",
                            "type": "text",
                            "text": "x.com/FredKSchott/st…",
                            "url": "https://x.com/FredKSchott/status/1800548938434642119"
                        }
                    ]
                }
            ]
        }
    ]
""".trimIndent()

private val bookmarkDetailListType = object : TypeToken<List<BookmarkDetail>>() {}.type

val mockBookmarkDetails = customTypeAdapter.fromJson<List<BookmarkDetail>>(
    mockedBookmarkDetailsData,
    bookmarkDetailListType
)