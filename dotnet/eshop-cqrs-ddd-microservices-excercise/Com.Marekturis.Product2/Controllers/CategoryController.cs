﻿using System.Net;
using System.Web.Http;
using Com.Marekturis.Product2.Model.Application;
using Com.Marekturis.Product2.Model.Application.Authorization;
using Com.Marekturis.Product2.Model.Application.Dto;

namespace Com.Marekturis.Product2.Controllers
{
    public class CategoryController : ApiController
    {
        private readonly CategoryApplicationService categoryService;

        public CategoryController(CategoryApplicationService categoryService)
        {
            this.categoryService = categoryService;
        }

        public IHttpActionResult Post(CreateCategoryDto dto)
        {
            dto.ExecutorToken = this.GetAuthorizationToken();
            if (!ModelState.IsValid)
            {
                return StatusCode(HttpStatusCode.BadRequest);
            }

            try
            {
                categoryService.AddCategory(dto);
            }
            catch (AuthorizationException)
            {
                return StatusCode(HttpStatusCode.Unauthorized);
            }

            return StatusCode(HttpStatusCode.Created);
        }

        public IHttpActionResult Delete(int id)
        {
            var dto = new DeleteCategoryDto()
            {
                ExecutorToken = this.GetAuthorizationToken(),
                Id = id
            };

            if (!ModelState.IsValid)
            {
                return StatusCode(HttpStatusCode.BadRequest);
            }

            try
            {
                categoryService.DeleteCategory(dto);
            }
            catch (AuthorizationException)
            {
                return StatusCode(HttpStatusCode.Unauthorized);
            }

            return StatusCode(HttpStatusCode.NoContent);
        }
    }
}