package com.financial.experts.module.blog.service;

import com.financial.experts.database.postgres.entity.BlogPost;
import com.financial.experts.database.postgres.repository.BlogPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogService blogService;

    private BlogPost blogPost;
    @BeforeEach
    void setUp() {
        this.blogPost = new BlogPost();
        blogPost.setTitle("Test title");
        blogPost.setContent("Test content");
        blogPost.setId(1L);
        blogPost.setUpdatedAt(LocalDateTime.now());
        blogPost.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldCreateBlogPost(){

        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        BlogPost createdPost = blogService.createBlogPost(blogPost);

        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getTitle()).isEqualTo(blogPost.getTitle());
        assertThat(createdPost.getContent()).isEqualTo(blogPost.getContent());
        assertThat(createdPost.getId()).isEqualTo(blogPost.getId());
        assertThat(createdPost.getCreatedAt()).isEqualTo(blogPost.getCreatedAt());

    }
    @Test
    void shouldUpdateBlogPostSuccessfully(){
        when(blogPostRepository.findById(1L)).thenReturn(Optional.ofNullable(blogPost));
        blogPost.setContent("Updated content");
        blogPost.setTitle("Updated title");
        LocalDateTime now =LocalDateTime.now();
        blogPost.setUpdatedAt(now);
        when(blogPostRepository.save(blogPost)).thenReturn(blogPost);

        BlogPost updatedPost = blogService.updateBlogPost(1L, blogPost);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo(blogPost.getTitle());
        assertThat(updatedPost.getContent()).isEqualTo(blogPost.getContent());
        assertThat(updatedPost.getId()).isEqualTo(blogPost.getId());
        assertThat(updatedPost.getCreatedAt()).isEqualTo(blogPost.getCreatedAt());
        assertThat(updatedPost.getUpdatedAt())
                .isCloseTo(now, Assertions.within(1, ChronoUnit.SECONDS));

        verify(blogPostRepository, times(1)).save(blogPost);
    }
    @Test
    void shouldThrowExceptionWhenBlogPostNotFound(){
        when(blogPostRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> blogService.updateBlogPost(1L, blogPost));
    }

    @Test
    void shouldReturnAllBlogPosts(){
        BlogPost blogPost1 = new BlogPost();
        blogPost1.setTitle("Title 1");
        blogPost1.setContent("Content 1");
        blogPost1.setId(1L);
        blogPost1.setCreatedAt(LocalDateTime.now());
        blogPost1.setUpdatedAt(LocalDateTime.now());

        BlogPost blogPost2 = new BlogPost();
        blogPost2.setTitle("Title 2");
        blogPost2.setContent("Content 2");
        blogPost2.setId(2L);
        blogPost2.setCreatedAt(LocalDateTime.now());
        blogPost2.setUpdatedAt(LocalDateTime.now());

        List<BlogPost> blogPosts = Arrays.asList(blogPost1, blogPost2);

        when(blogPostRepository.findAll()).thenReturn(blogPosts);

        List<BlogPost> allBlogPosts = blogService.getAllBlogPosts();

        assertThat(allBlogPosts).isNotNull();
        assertThat(allBlogPosts.size()).isEqualTo(2);
        assertThat(allBlogPosts).isEqualTo(blogPosts);
        verify(blogPostRepository, times(1)).findAll();

    }


}