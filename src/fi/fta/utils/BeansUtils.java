package fi.fta.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import fi.fta.beans.Category;
import fi.fta.beans.MetaData;
import fi.fta.beans.MetaDataFormat;
import fi.fta.beans.MetaDataSource;
import fi.fta.beans.TypedLayerService;
import fi.fta.beans.WFS;
import fi.fta.beans.WMS;
import fi.fta.beans.WMSStyle;
import fi.fta.beans.ui.CategoryCountsUI;
import fi.fta.beans.ui.CategorySummaryUI;
import fi.fta.beans.ui.MetaDataUI;
import fi.fta.beans.ui.TreeBranchUI;
import fi.fta.beans.ui.TreeCategoryLayerUI;
import fi.fta.beans.ui.TreeCategoryUI;
import fi.fta.beans.ui.TreeLayerUI;
import fi.fta.beans.ui.TreeServiceUI;
import fi.fta.beans.ui.TreeWFSLayerUI;
import fi.fta.beans.ui.TreeWMSLayerUI;
import fi.fta.beans.ui.WMSStyleUI;
import fi.fta.data.managers.CategoryManager;
import fi.fta.model.SiteModel;
import fi.fta.utils.parse.wms.Style;

public class BeansUtils
{
	
	public static List<MetaData> getMetaDataFromUrls(Collection<String> urls, MetaDataSource source)
	{
		List<MetaData> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(urls))
		{
			for (String url : urls)
			{
				MetaData md = new MetaData();
				md.setFormat(MetaDataFormat.UNKNOWN);
				md.setUrl(url);
				md.setSource(source);
				ret.add(md);
			}
		}
		return ret;
	}
	
	public static List<MetaData> getMetaData(
		Collection<? extends fi.fta.utils.parse.MetaData> collection, MetaDataSource source)
	{
		List<MetaData> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(collection))
		{
			for (fi.fta.utils.parse.MetaData mdu : collection)
			{
				ret.add(new MetaData(mdu, source));
			}
		}
		return ret;
	}
	
	public static List<MetaDataUI> getMetaDataUI(Collection<MetaData> collection)
	{
		List<MetaDataUI> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(collection))
		{
			for (MetaData md : collection)
			{
				ret.add(new MetaDataUI(md));
			}
		}
		return ret;
	}
	
	public static boolean contains(Collection<MetaData> collection, MetaData bean)
	{
		if (!Util.isEmptyCollection(collection) && bean != null)
		{
			for (MetaData md : collection)
			{
				if (bean.getUrl().equalsIgnoreCase(md.getUrl()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<WMSStyle> getStyles(Collection<Style> collection)
	{
		List<WMSStyle> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(collection))
		{
			for (Style s : collection)
			{
				ret.add(new WMSStyle(s));
			}
			ret.get(0).setMain(true);
		}
		return ret;
	}
	
	public static List<WMSStyleUI> getStylesUI(Collection<WMSStyle> collection)
	{
		List<WMSStyleUI> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(collection))
		{
			for (WMSStyle s : collection)
			{
				ret.add(new WMSStyleUI(s));
			}
		}
		return ret;
	}
	
	public static List<TreeBranchUI> getLayerUIs(
		Long parent, SiteModel m) throws HibernateException 
	{
		boolean can = m == null || m.canReadThis(parent);
		List<TreeBranchUI> ret = new ArrayList<>();
		if (parent == null)
		{
			ret.addAll(BeansUtils.getLayerUIs(
				CategoryManager.getInstance().getRoot(), m, can));
		}
		else
		{
			ret.addAll(BeansUtils.getLayerUIs(
				CategoryManager.getInstance().getChildren(parent), m, can));
		}
		if (can)
		{
			ServiceCollections services = new ServiceCollections(parent);
			if (!services.isEmpty())
			{
				Category c = new Category();
				if (parent != null)
				{
					c = CategoryManager.getInstance().get(parent);
				}
				ret.add(new TreeLayerUI(c, services));
			}
		}
		return ret;
	}
	
	public static List<TreeBranchUI> getLayerUIs(
		Collection<Category> root, SiteModel m, boolean can) throws HibernateException 
	{
		List<TreeBranchUI> layers = new ArrayList<>();
		for (Category c : root)
		{
			boolean ccan = can || m == null || m.canReadThis(c.getId());
			if (ccan)
			{
				ServiceCollections services = new ServiceCollections(c.getId());
				if (services.isEmpty())
				{
					TreeCategoryUI ui = new TreeCategoryUI(c);
					ui.getLayers().addAll(BeansUtils.getLayerUIs(c.getChildren(), m, ccan));
					layers.add(ui);
				}
				else if (c.getChildren().isEmpty())
				{
					layers.add(new TreeLayerUI(c, services));
				}
				else
				{
					TreeCategoryLayerUI ui = new TreeCategoryLayerUI(c, services);
					ui.getLayers().addAll(BeansUtils.getLayerUIs(c.getChildren(), m, ccan));
					layers.add(ui);
				}
			}
			else
			{
				layers.addAll(BeansUtils.getLayerUIs(c.getChildren(), m, ccan));
			}
		}
		return layers;
	}
	
	public static List<TreeServiceUI> getTreeServiceUIs(Collection<TypedLayerService> services)
	{
		List<TreeServiceUI> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(services))
		{
			for (TypedLayerService s : services)
			{
				ret.add(new TreeServiceUI(s));
			}
		}
		return ret;
	}
	
	public static List<TreeWMSLayerUI> getTreeWMSUIs(Collection<WMS> wmses)
	{
		List<TreeWMSLayerUI> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(wmses))
		{
			for (WMS wms : wmses)
			{
				ret.add(new TreeWMSLayerUI(wms));
			}
		}
		return ret;
	}
	
	public static List<TreeWFSLayerUI> getTreeWFSUIs(Collection<WFS> wfses)
	{
		List<TreeWFSLayerUI> ret = new ArrayList<>();
		if (!Util.isEmptyCollection(wfses))
		{
			for (WFS wfs : wfses)
			{
				ret.add(new TreeWFSLayerUI(wfs));
			}
		}
		return ret;
	}
	
	public static List<CategorySummaryUI> getSummary(Long id, Integer depth) throws HibernateException
	{
		List<CategorySummaryUI> summaries = new ArrayList<>();
		if (id == null)
		{
			if (depth == null || depth.intValue() == 0)
			{
				summaries.addAll(BeansUtils.getSummary(
					CategoryManager.getInstance().getRoot()));
			}
			else if (depth.intValue() > 0)
			{
				summaries.addAll(BeansUtils.getSummaryDown(
					CategoryManager.getInstance().getRoot(), depth));
			}
			else
			{
				summaries.addAll(BeansUtils.getSummaryUp(
					CategoryManager.getInstance().getYoungest(), depth));
			}
		}
		else
		{
			Category c = CategoryManager.getInstance().get(id);
			if (c != null)
			{
				if (depth == null || depth.intValue() == 0)
				{
					summaries.addAll(BeansUtils.getSummary(
						Collections.singletonList(c)));
				}
				else if (depth.intValue() > 0)
				{
					summaries.addAll(BeansUtils.getSummaryDown(
						Collections.singletonList(c), depth));
				}
				else
				{
					summaries.addAll(BeansUtils.getSummaryUp(
						BeansUtils.getYoungest(c), depth));
				}
			}
		}
		return summaries;
	}
	
	public static List<CategorySummaryUI> getSummary(List<Category> categories) throws HibernateException
	{
		List<CategorySummaryUI> ret = new ArrayList<>();
		for (Category c : categories)
		{
			CategorySummaryUI ui = new CategorySummaryUI(c);
			ui.setCounts(new CategoryCountsUI(c));
			if (!Util.isEmptyCollection(c.getChildren()))
			{
				ui.setChildren(BeansUtils.getSummary(c.getChildren()));
				ui.getChildren().forEach(
					(csui)->{ui.getCounts().sum(csui.getCounts());});	
			}
			ret.add(ui);
		}
		return ret;
	}
	
	public static List<CategorySummaryUI> getSummaryDown(List<Category> categories, int depth) throws HibernateException
	{
		List<CategorySummaryUI> ret = new ArrayList<>();
		for (Category c : categories)
		{
			CategorySummaryUI ui = new CategorySummaryUI(c);
			ui.setCounts(new CategoryCountsUI(c));
			if (!Util.isEmptyCollection(c.getChildren()))
			{
				if (depth > 1)
				{
					ui.setChildren(BeansUtils.getSummaryDown(c.getChildren(), depth-1));
					ui.getChildren().forEach(
						(csui)->{ui.getCounts().sum(csui.getCounts());});
				}
				else
				{
					BeansUtils.getSummary(c.getChildren()).forEach(
						(csui)->{ui.getCounts().sum(csui.getCounts());});
				}
			}
			ret.add(ui);
		}
		return ret;
	}
	
	public static List<CategorySummaryUI> getSummaryUp(List<Category> categories, int depth) throws HibernateException
	{
		return BeansUtils.getSummary(BeansUtils.getParents(categories, depth));
	}
	
	public static List<Category> getParents(List<Category> categories, int depth)
	{
		Map<Long, Category> m = new HashMap<>();
		for (Category c : categories)
		{
			int d = depth;
			Category p = c;
			while (d < -1 && p.getParent() != null)
			{
				p = p.getParent();
				d++;
			}
			m.put(p.getId(), p);
		}
		List<Category> parents = new ArrayList<>(m.values());
		Collections.sort(parents, (p1, p2)->{return p1.getPosition().compareTo(p2.getPosition());});
		return parents;
	}
	
	public static List<Category> getYoungest(Category c)
	{
		List<Category> ret = new ArrayList<>();
		if (Util.isEmptyCollection(c.getChildren()))
		{
			ret.add(c);
		}
		else
		{
			c.getChildren().forEach(
				(cc)->{ret.addAll(BeansUtils.getYoungest(cc));});
		}
		return ret;
	}
	
}
