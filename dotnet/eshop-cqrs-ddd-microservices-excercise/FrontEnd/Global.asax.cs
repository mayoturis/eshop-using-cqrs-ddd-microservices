﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using Castle.MicroKernel;
using Castle.Windsor;
using Com.Marekturis.Common.Application.Authorization;
using Com.Marekturis.Common.Infrastructure.Remote;

namespace FrontEnd
{
    public class MvcApplication : HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);

            var container = new WindsorContainer();
            container.Install(new FrontEndMapping());

            var controllerFactory = new WindsorControllerFactory(container.Kernel);
            ControllerBuilder.Current.SetControllerFactory(controllerFactory);
        }

        protected void Application_Error(object sender, EventArgs e)
        {
            HttpContext httpContext = HttpContext.Current;
            var exception = Server.GetLastError();
            if (exception is AuthenticationException)
            {
                Response.Redirect("~/Authentication/Login");
                return;
            }

            if (exception is AuthorizationException)
            {
                httpContext.Session["error"] = "Unauthorized to perform given action";
                Response.Redirect("~/Authentication/Login");
                return;
            }
        }

        public class WindsorControllerFactory : DefaultControllerFactory
        {
            private readonly IKernel _kernel;

            public WindsorControllerFactory(IKernel kernel)
            {
                this._kernel = kernel;
            }

            public override void ReleaseController(IController controller)
            {
                _kernel.ReleaseComponent(controller);
            }

            protected override IController GetControllerInstance(RequestContext requestContext, Type controllerType)
            {
                if (controllerType == null)
                {
                    throw new HttpException(404, $"The controller for path '{requestContext.HttpContext.Request.Path}' could not be found.");
                }
                return (IController)_kernel.Resolve(controllerType);
            }
        }
    }
}
