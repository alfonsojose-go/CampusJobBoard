# **Campus Job Board**

A full-stack job portal for educational institutions with role-based access control, CRUD operations, and intelligent category filtering.

<p>
  <img src="https://img.shields.io/badge/React-18-blue?style=flat-square" alt="React 18" />
</p>
<p>
  <img src="https://img.shields.io/badge/Spring%20Boot-3-green?style=flat-square" alt="Spring Boot 3" />
</p>
</p>
  <img src="https://img.shields.io/badge/Maven-3.9.0-red?style=flat-square" alt="Maven 3.9.0" />
</p>


## **Problem Statement**

University career centers struggle with:

- **Fragmented job posting systems** — Employers email spreadsheets; students check bulletin boards
  
- **No access control** — Anyone can view or edit any posting, leading to spam and vandalism

- **Poor discoverability** — Students can't filter by major, job type, or location
  
- **Manual approval workflows** — Staff spend hours reviewing and publishing postings
  
Campus Job Board provides a centralized, secure platform where administrators control the ecosystem, employers manage their postings, and students find relevant opportunities.

## **Tech Stack**

| Layer             | Technology                   | Purpose                                               |
| ----------------- | ---------------------------- | ----------------------------------------------------- |
| Backend Framework | Spring Boot 3                | Enterprise-grade MVC architecture                     |
| Template Engine   | Thymeleaf                    | Server-side rendering for SEO-friendly pages          |
| Build Tool        | Maven                        | Dependency management and build automation            |
| Database          | H2 (dev) / PostgreSQL (prod) | Lightweight development, production-ready persistence |
| Security          | Spring Security              | Role-based authentication and authorization           |
| ORM               | Spring Data JPA              | Database abstraction and query generation             |
| Validation        | Jakarta Bean Validation      | Input sanitization and business rule enforcement      |

## **Key Features**
- **Role-based access control (RBAC)** — Three distinct user types with granular permissions:
  - Admin: Full platform control, user management, analytics
  - Employer: Create, edit, and manage own job postings; view application metrics
  - Student: Browse, filter, and bookmark job listings; track applications
    
      
- **Category filtering system** — Dynamic filtering by job type, location, salary range, and required skills
  
- **CRUD operations** — Full create, read, update, delete functionality for all user-generated content
  
- **Application tracking** — Students mark applications as "applied," "interviewing," or "offered"
  
- **Admin moderation queue** — Employer postings require admin approval before going live
  
- **Responsive design** — Mobile-first Thymeleaf templates with Bootstrap 5

## **Architecture Decisions**

**Why server-side rendering with Thymeleaf instead of a React SPA?**

For a job board, SEO matters. Search engines need to index job postings. A Spring Boot + Thymeleaf stack ensures every job listing is a crawlable HTML page with proper meta tags, structured data, and semantic markup. A React SPA would require complex SSR (Next.js) or prerendering infrastructure.
Additionally, this project was built in an academic context where demonstrating mastery of enterprise Java patterns (MVC, dependency injection, layered architecture) was a learning objective. Thymeleaf integrates natively with Spring's form binding and validation—showing fluency in the full Spring ecosystem.

**Why H2 for development and PostgreSQL for production?**

H2 provides an in-memory database that requires zero setup—ideal for rapid development and peer review. The JPA abstraction means switching to PostgreSQL is a single configuration change, demonstrating understanding of environment-specific configuration management.

## **Challenges & Solutions**

**Challenge: Complex authorization rules across roles**

Spring Security's default role-based access is binary (has role / doesn't have role). Campus Job Board needed object-level permissions—an employer can edit their postings but not another employer's postings.

**Solution:** Implemented a custom PermissionEvaluator that checks ownership at runtime. Combined with method-level security (@PreAuthorize), this enabled fine-grained rules like:
```Java
@PreAuthorize("hasRole('EMPLOYER') and @jobPostingService.isOwner(#id, authentication.name)")
public String editJobPosting(@PathVariable Long id, Model model) { ... }
```

**Challenge: Category filter performance with large datasets**

With 500+ job postings, the category filter query became slow due to N+1 SELECT issues from lazy-loaded relationships.

**Solution:** Refactored to Entity Graphs and JOIN FETCH queries. This reduced the filter endpoint from 12 database queries to 1, cutting response time from 1.2s to 180ms.

**Challenge: Form validation UX**
Server-side validation errors required full page reloads, destroying user input and creating a frustrating experience.

**Solution:** Combined Thymeleaf's form error binding with HTMX for partial page updates. Validation errors now appear inline without page refreshes, while maintaining the SEO benefits of server-side rendering.

## **How to Run**

**Prerequisites**
- Java 17+
- Maven 3.9+
- PostgreSQL 14+ (for production mode)

**Development (H2 in-memory)**

```bash

git clone https://github.com/alfonsojose-go/CampusJobBoard.git
cd CampusJobBoard
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```


**Production (PostgreSQL)**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

The application will be available at http://localhost:8080.

**Default Accounts**

| Role     | Username               | Password    |
| -------- | ---------------------- | ----------- |
| Admin    | <admin@campus.edu>     | admin123    |
| Employer | <employer@company.com> | employer123 |
| Student  | <student@campus.edu>   | student123  |

## **What I Learned**

**Spring Security depth:** RBAC sounds simple until you need object-level permissions. Building a custom PermissionEvaluator taught me how Spring Security's authorization architecture actually works under the hood.

**Database query optimization:** The N+1 problem is invisible in development with 10 records. It becomes catastrophic in production. I learned to profile queries with Hibernate statistics and optimize with Entity Graphs.

**Progressive enhancement:** Adding HTMX to a traditional server-rendered app showed me that modern UX doesn't require abandoning proven architectures. You can have crawlable pages and responsive interactions.


## **Links**
- [GitHub Repository](https://github.com/alfonsojose-go/CampusJobBoard)

**License:** MIT


