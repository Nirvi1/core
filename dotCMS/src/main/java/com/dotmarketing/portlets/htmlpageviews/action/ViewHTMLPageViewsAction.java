package com.dotmarketing.portlets.htmlpageviews.action;

import javax.servlet.jsp.PageContext;

import com.dotcms.repackage.javax.portlet.PortletConfig;
import com.dotcms.repackage.javax.portlet.RenderRequest;
import com.dotcms.repackage.javax.portlet.RenderResponse;
import com.dotcms.repackage.javax.portlet.WindowState;
import com.dotcms.repackage.org.apache.struts.action.ActionForm;
import com.dotcms.repackage.org.apache.struts.action.ActionForward;
import com.dotcms.repackage.org.apache.struts.action.ActionMapping;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.web.HostWebAPI;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.cache.VirtualLinksCache;
import com.dotmarketing.exception.DotHibernateException;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portal.struts.DotPortletAction;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.htmlpageasset.model.IHTMLPage;
import com.dotmarketing.portlets.htmlpages.model.HTMLPage;
import com.dotmarketing.portlets.virtuallinks.factories.VirtualLinkFactory;
import com.dotmarketing.portlets.virtuallinks.model.VirtualLink;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.WebKeys;
import com.liferay.portal.language.LanguageUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.Constants;
import com.liferay.util.servlet.SessionMessages;

/**
 * <a href="ViewQuestionsAction.java.html"> <b><i>View Source </i> </b> </a>
 * 
 * @author Maria Ahues
 * @version $Revision: 1.5 $
 *  
 */
public class ViewHTMLPageViewsAction extends DotPortletAction {

	protected HostWebAPI hostAPI = WebAPILocator.getHostWebAPI();

	/*
     * @see com.liferay.portal.struts.PortletAction#render(com.dotcms.repackage.org.apache.struts.action.ActionMapping,
     *      com.dotcms.repackage.org.apache.struts.action.ActionForm, com.dotcms.repackage.javax.portlet.PortletConfig,
     *      com.dotcms.repackage.javax.portlet.RenderRequest, com.dotcms.repackage.javax.portlet.RenderResponse)
     */
    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req,
            RenderResponse res) throws Exception {

        Logger.debug(this, "Running ViewHTMLPagesAction!!!!");

        try {
            //gets the user
            User user = _getUser(req);

            if (req.getWindowState().equals(WindowState.NORMAL)) {
                return mapping.findForward("portlet.ext.htmlpageviews.view");
            } else {
                //Mailing lists



                /** @see com.dotmarketing.portal.struts.DotPortletAction._viewWebAssets * */
                _viewWebAssets(req, user);
                return mapping.findForward("portlet.ext.htmlpageviews.view_htmlpage_views");
            }
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(Constants.COMMON_ERROR);
        }
    }

 

    //Needs to be implemented instead of using parent method because we use
    // template to search for HTMLPages
    private void _viewWebAssets(RenderRequest req, User user) throws Exception {

    	User systemUser = APILocator.getUserAPI().getSystemUser();
    	
        String uri = null;
        Host host = null;
        if (req.getParameter("htmlpage") != null) {
        	Contentlet contentlet = APILocator.getContentletAPI().find(req.getParameter("htmlpage"), systemUser, false);
        	IHTMLPage myHTMLPage = null;
        	if (contentlet!=null){
        		myHTMLPage = APILocator.getHTMLPageAssetAPI().fromContentlet(contentlet);
        	}else
        		myHTMLPage = (IHTMLPage) InodeFactory.getInode(req.getParameter("htmlpage"), HTMLPage.class);
            uri = APILocator.getIdentifierAPI().find(myHTMLPage).getURI();
			host = hostAPI.findParentHost(myHTMLPage, systemUser, false);
            req.setAttribute("htmlPage", myHTMLPage);
        } else if (req.getParameter("pageIdentifier") != null) {
            //Identifier id = (Identifier) InodeFactory.getInode(req.getParameter("pageIdentifier"), Identifier.class);
        	Identifier id = APILocator.getIdentifierAPI().find(req.getParameter("pageIdentifier"));
            uri = id.getURI();
            IHTMLPage myHTMLPage = (IHTMLPage) APILocator.getVersionableAPI().findLiveVersion(id, APILocator.getUserAPI().getSystemUser(),false);
			host = hostAPI.findParentHost(myHTMLPage, systemUser, false);
            req.setAttribute("htmlPage", myHTMLPage);
        }
        
        if (req.getParameter("pageURL") != null) {
            uri = req.getParameter("pageURL");
            String[] parts = uri.split(":");            
            if (parts.length > 1) {
                host = hostAPI.findByName(parts[0], systemUser, false);
                uri = parts[1];
            } else {
                host = hostAPI.findDefaultHost(systemUser, false);
            }
                        
            Identifier id = APILocator.getIdentifierAPI().find(host, uri);
            IHTMLPage myHTMLPage = (IHTMLPage) APILocator.getVersionableAPI().findLiveVersion(id, APILocator.getUserAPI().getSystemUser(),false);
            req.setAttribute("htmlPage", myHTMLPage);
            if (!InodeUtils.isSet(id.getInode())) {

                VirtualLink vl = null;
                try{
                	vl = VirtualLinkFactory.getVirtualLinkByURL(uri);
                }
                catch(DotHibernateException dhe){
                	Logger.debug(VirtualLinksCache.class, "failed to find: " + uri);  
                }
                if (vl != null && !InodeUtils.isSet(vl.getInode())) {

                    myHTMLPage.setTitle(LanguageUtil.get(user, "message.htmlpageviews.pagenotfound"));
                    SessionMessages.add(req, "message", "message.htmlpageviews.pagenotfound");
                } else {
                    req.setAttribute(WebKeys.VIRTUAL_LINK_EDIT, vl);
                }
            }
        }
        
        req.setAttribute("uri", uri);        
 
        Logger.debug(this, "Done with ViewHTMLPageViewsAction");

    }

 
}