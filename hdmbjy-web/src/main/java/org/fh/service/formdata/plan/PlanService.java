package org.fh.service.formdata.plan;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fh.entity.Page;
import org.fh.entity.PageData;

/** 
 * 说明： 民办培训学校设立教学计划列表接口
 * 作者：FH Admin QQ313596790
 * 时间：2019-12-05
 * 官网：www.fhadmin.org
 * @version
 */
public interface PlanService{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;


	int batchSave(@Param("list")List<PageData> list);

	List<PageData> findByTeachdingProgramId(PageData pd);

	int deleteByTeachdingProgramId(PageData pd);
	
}
