<template>
  <div class="table-container" style="width:100%;">
<el-dialog title="Ajouter une famille" :visible.sync="dialogFormVisible">
      <el-form :model="modelFamily" status-icon :rules="rules" ref="modelFamily"  class="demo-ruleForm">
	  
        <el-form-item label=""  required prop="fatherFirstName">
          <el-input v-model="modelFamily.fatherFirstName" placeholder="Prénom de père"></el-input>
        </el-form-item>
        <el-form-item label=""  required prop="motherFirstName">
          <el-input v-model="modelFamily.motherFirstName" placeholder="Prénom de mère"></el-input>
        </el-form-item>
		<el-form-item label=""  required prop="fatherPhoneNumber">
          <el-input v-model="modelFamily.fatherPhoneNumber" placeholder="téléphone de père"></el-input>
        </el-form-item>
		<el-form-item label=""  required prop="motherPhoneNumber">
          <el-input v-model="modelFamily.motherPhoneNumber" placeholder="téléphone de mère"></el-input>
        </el-form-item>
		<el-form-item label=""  required prop="fatherEmail">
          <el-input v-model="modelFamily.fatherEmail" placeholder="Email de père"></el-input>
        </el-form-item>
		<el-form-item label=""  required prop="motherEmail">
          <el-input v-model="modelFamily.motherEmail" placeholder="Email de mère"></el-input>
        </el-form-item>
		<el-form-item label=""  required prop="address">
          <el-input v-model="modelFamily.address" placeholder="adresse de famille"></el-input>
        </el-form-item>
        <el-row>
        </el-row>
       
        <el-form-item style="text-align:right;">
          <el-button  @click="resetForm('modelFamily')" plain ><i class="el-icon-close" ></i> Reset</el-button>
          <el-button type="success" @click="addFamily('modelFamily')"  ><i class="el-icon-check" ></i> Ajouter</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

          <el-button style="float:right;margin-top: 6px;margin-bottom: 10px" type="primary"  @click="dialogFormVisible = true"> <i class="el-icon-plus"></i> Ajouter une famille</el-button>
       
	<el-table
            :data="families.slice((currentPage-1)*pagesize, currentPage*pagesize).filter(data => !search || data._id.toLowerCase().includes(search.toLowerCase()))" class="shadow-table">

            <el-table-column
              label="familyId"
              prop="_id"
             >
            </el-table-column>
            <el-table-column
              label="Prénom de père"
              prop="fatherFirstName"
             >
            </el-table-column>

          <el-table-column
              label="Prénom de mère"
              prop="motherFirstName"
             >
            </el-table-column>
            <el-table-column
              label="téléphone de père"
              prop="fatherPhoneNumber"
             >
            </el-table-column>
            <el-table-column
              label="téléphone de mère"
              prop="motherPhoneNumber"
            >
			</el-table-column>
			
			 <el-table-column
              label="Email de père"
              prop="fatherEmail"
            >
			</el-table-column>
			
			 <el-table-column
              label="Email de père"
              prop="motherEmail"
            >
			</el-table-column>
			
			 <el-table-column
              label="adress de famille"
              prop="address"
            >
			</el-table-column>
             

            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  placeholder="Trouver une famille .."/>
              </template>
              <template scope="scope">
                <el-tooltip class="item" content="Modifier cet enfant" placement="top-start">
                  <el-button
                    type="success"
                    icon="el-icon-edit"
                    circle
                    @click="handleEdit(scope.$index,scope.row)" >
                  </el-button>
                </el-tooltip>
                <el-tooltip class="item" content="Détail de paiement" placement="top-start">
                  <el-button
                    type="success"
                    icon="el-icon-view"
                    circle
                    @click="handleEdit(scope.$index,scope.row)" >
                  </el-button>
                </el-tooltip>
                
              </template>
            </el-table-column>
          </el-table>

      <!-- Pagination -->
      <el-row align="center">
        <el-col :span="24" align="center">
          <br/>
          <el-pagination
            background
            :page-size="5"
            :pager-count="5"
            layout="prev, pager, next"
            :total="families.length"
            @current-change="current_change">
          </el-pagination>
        </el-col>
      </el-row>
	</div>
	</template>
   <script>
    import {createNamespacedHelpers} from 'vuex'
    import { mapGetters ,mapActions,mapState} from 'vuex'
    import moment from 'moment'
    import axios from 'axios';
 
    export default {
        
        data() {
          return {
                total: 0,
                pagesize:5,
                currentPage:1,
       
                listLoading: false,
                dialogTableVisible: false,
                dialogFormVisible: false,
                dialogFormUpdateVisible: false,
                dialogVisible: false,
                dialogDeleteVisible: false,
                modelFamily: {
                    _id: '',
                    fatherFirstName: '',
                    motherFirstName: '',
                    fatherPhoneNumber: '',
					motherPhoneNumber: '',
					fatherEmail: '',
					motherEmail: '',
					address: '',
                },
                
                formLabelWidth: '120px',
                search: '',
                preview: null
          }


        },
        mounted() {
               this.$store.dispatch('family/FETCH_FAMILIES')
             

        },

        computed: {
            ...mapState(
                {  families: state => state.family.families}
                    
            ),
     
            ...mapGetters(['family/FAMILIES']),

        },
        methods: {
            ...mapActions({
                add: "family/ADD_FAMILY",
                delete:"family/DELETE_FAMILY",
                update:"family/UPDATE_FAMILY"},
            ),
            addFamily(modelFamily) {
               
                    this.$refs[modelFamily].validate((valid) => {
                        if (valid) {

                            try {
                                this.$store.dispatch('family/ADD_FAMILY', {

                                    payload: this.modelFamily


                                }).then(() => {

                                    this.$notify({
                                        title: 'Success',
                                        message: 'Un enfant est ajouté avec succès !',
                                        type: 'success'
                                    });
                                    this.resetForm(modelFamily)
                                    this.dialogFormVisible = false

                                })
                            }
                            catch (e) {


                                this.$notify({
                                    title: 'Erreur',
                                    message: 'Une erreur c\'est produite!',
                                    type: 'error'
                                });

                            }
                        }

                        else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
            },
            updateFamily(editFamily){
                
                    this.$refs[editFamily].validate((valid) => {
                        if (valid) {
                            this.update(this.editFamily)
                            this.$notify({
                                title: 'Succès ',
                                message: 'Enfant  modifée avec succés!',
                                type: 'success'
                            });
                            this.dialogFormUpdateVisible = false

                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
            },

            deleteFamily(){
                this.delete(this.formModelFamily)
                this.$message({
                    message: 'Enfant supprimé avec avec succès !',
                    type: 'error'
                });         this.dialogDeleteVisible = false
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();

            },
            handleEdit(index, row) {
                console.log(index, row);
                this.dialogFormUpdateVisible = true;
                this.editFamily = Object.assign({}, row);
            },
            handleRead(index, row) {
                console.log(index, row);
                this.dialogVisible = true;
                this.editFamily = Object.assign({}, row);
            },
            handleDelete(index, row) {
                this.formModelFamily=Object.assign({}, row);
                this.dialogDeleteVisible=true
            },
            current_change:function(currentPage){
                this.currentPage = currentPage;
            },
            toReadable: (date) => {
                return moment(date).format('DD/MM/YYYY')
            },
           
      
        }

    }
</script>