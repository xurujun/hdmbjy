package org.fh.service.formdata.fullteacherform.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.fh.entity.Page;
import org.fh.entity.PageData;
import org.fh.mapper.dsno1.formdata.fullteacherform.FullTeacherFormMapper;
import org.fh.service.formdata.fullteacherform.FullTeacherFormService;

/** 
 * 说明： 幼儿园专职教师名册接口实现类
 * 作者：FH Admin Q313596790
 * 时间：2019-11-05
 * 官网：www.fhadmin.org
 * @version
 */
@Service
@Transactional //开启事物
public class FullTeacherFormServiceImpl implements FullTeacherFormService{

	@Autowired
	private FullTeacherFormMapper fullteacherformMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		fullteacherformMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		fullteacherformMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		fullteacherformMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return fullteacherformMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return fullteacherformMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return fullteacherformMapper.findById(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		fullteacherformMapper.deleteAll(ArrayDATA_IDS);
	}
	
}

