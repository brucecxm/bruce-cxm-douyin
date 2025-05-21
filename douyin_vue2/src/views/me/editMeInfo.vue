<template>
  <div class="app-container">
    <el-upload
      class="upload-demo"
      :http-request="customUpload"
      :file-list="myFileList"
      :data="uploadData"
      :limit="1"
      :before-upload="(file) => beforeUpload(file, 'my')"
      list-type="picture"
    >
      >
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">
        只能上传jpg/png文件，且不超过500kb
      </div>
    </el-upload>
  </div>
</template>

<script>
export default {
  data() {
    return {
      forDel:{},
      _TaskId: '', // 任务ID
      _TaskConfig:{},
      uploadData: {
        theme: 'home-page-theme'
      },
      allThemeState: [
        { ThemeThemeStateValue: 'A', ThemeThemeStateName: '开启' },
        { ThemeThemeStateValue: 'I', ThemeThemeStateName: '关闭' }
      ],

      additem:'',
      fileList: [],
      titleFlag:'',
      ThemeQuery: {
        ThemeTypeState: '',
        ThemeState: '',
        ThemeTypeCode: '',
        ThemeNum: '',
        IsDef: '',
        ThemeDesc: '',
        ThemeName: '',
        ThemeCode: '',
        _PageIndex: 1,
        _PageSize: 10
      },
      isConfirmClose: false,
      dialogStatus: '',
      ThemeTypeAll:{},
      ThemeList: [

      ],
      color1: '#409EFF',
      total: 0,
      ThemeCodeShow:false,
      listLoading: false,
      dialogFormVisible: false,
      dialogTitle: '',
      tempTheme: {
        IsDef:false,
        ThemeId: '',
        ThemeName: '',
        ThemeDesc: '',
        ThemeTypeCode: '',
        ThemeNum: '',
        ThemeTableColor: 'red',
        ThemeHomeImg: '',
        ThemewealImg: '',
        ThemeLoanImg: '',
        ThemeMyImg: '',
        ThemeCode: '',
        List: [],
      },
      fileUrl:'',
      // fileUrl:'http://yun1.dcitscloudtech.com:8866/fpaas_bucket/38498846_default/h5/',
      forUpdateStatus:{} ,
      deletePendingList: [],     // 编辑模式下待删除的图片记录
      homeFileList: [],
      loanFileList: [],
      wealFileList: [],
      currentRow: null,
      pendingValue: null,
      myFileList: [],
      authorModel: false,
      allThemeType: [      ],
      rules: {
        // 皮肤名称校验：只能输入汉字且最多4个汉字
        ThemeName: [
          { required: true, message: '请输入皮肤名称', trigger: 'blur' },
          { validator: this.validateChineseCharacters, message: '皮肤名称只能输入汉字', trigger: 'blur' },
          { max: 4, message: '皮肤名称最多允许输入4个汉字', trigger: 'blur' }
        ],
        IsDef:[ { required: true, message: '请选择是否为默认主题', trigger: 'change' },],
        // 皮肤说明校验：最多12个汉字
        ThemeDesc: [
          {
          { validator: this.validateChineseCharacters, message: '皮肤说明只能输入汉字', trigger: 'blur' },
          { max: 12, message: '皮肤说明最多允许输入12个汉字', trigger: 'blur' }
        ],

        // 皮肤大类校验：选择框，必选
        ThemeTypeCode: [
          { required: true, message: '请选择皮肤大类', trigger: 'change' }
        ],

        // 排序号码校验：必须是大于0小于99的正整数
        ThemeNum: [
          { required: true, message: '排序号码必须大于0', trigger: 'blur' },
          { validator: this.validateThemeNum, message: '排序号码必须是大于0小于99的正整数', trigger: 'blur' }
        ],

        ThemeTableColor: [
          { required: true, message: '请选择主题颜色', trigger: 'change' }
        ]
      },
    };
  },

  methods: {
    handleRemove(file, fileList) {
      console.log('删除文件:', file);
    },

    async customUpload(file, type) {
      return new Promise((resolve) => {
        console.log('开始上传文件:', file);
        // 模拟上传成功
        setTimeout(() => {
          resolve(true);
        }, 1000);
      });
    }
    ,
    beforeUpload(file, type) {
      return new Promise(async (resolve, reject) => {
        try {
          const success = await this.customUpload(file, type);
          if (success) {
            resolve();
          } else {
            reject(new Error('文件上传失败'));
          }
        } catch (error) {
          reject(error);
        }
      });
    }
    ,



  }
};
</script>

<style scoped>
.uploaded-image {
  max-width: 100%;
  /* 限制最大宽度为容器的100% */
  max-height: 200px;
  /* 限制最大高度 */
  object-fit: cover;
  /* 保持比例并填充容器 */
  z-index: 10;
  /* 确保图片在其他元素之上 */
  position: absolute;
  /* 使 z-index 生效 */
}
</style>
