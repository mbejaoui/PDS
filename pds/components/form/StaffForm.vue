<template>
  <div class="table-container" style="width:100%;">
<el-dialog title="Ajouter un employer" :visible.sync="dialogFormVisible">
      <el-form :model="modelStaff" status-icon :rules="rules" ref="modelStaff"  class="demo-ruleForm">
        <el-row>
          <el-col :span="8">
            <el-form-item label="" prop="gender" required>
              <el-radio-group v-model="modelStaff.gender">
                <el-radio label="Homme"></el-radio>
                <el-radio label="Femme"></el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col> 
          <el-col :span="7">
            <el-form-item label="" prop="dateOfBirth">
              <el-date-picker placeholder="Date de naissance" type="date" format="yyyy-MM-dd" v-model="modelStaff.dateOfBirth" style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col> 
        </el-row>
        <el-form-item label=""  required prop="firstName">
          <el-input v-model="modelStaff.firstName" placeholder="Nom de l'emplyée"></el-input>
        </el-form-item>
        <el-form-item label=""  required prop="lastName">
          <el-input v-model="modelStaff.lastName" placeholder="Prénom de l'employée"></el-input>
        </el-form-item>
        <el-form-item label=""  required prop="functions">
       <el-select
  v-model="modelStaff.functions"
  placeholder="Selectionnee une fonction"
>
  <el-option
    v-for="item in functions"
    :key="item.function"
    :label="item.function"
  />
</el-select>
        </el-form-item>
        <el-row>
        </el-row>
       
        <el-form-item style="text-align:right;">
          <el-button  @click="resetForm('modelStaff')" plain ><i class="el-icon-close" ></i> Reset</el-button>
          <el-button type="success" @click="addStaff('modelStaff')"  ><i class="el-icon-check" ></i> Ajouter</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

          <el-button style="float:right;margin-top: 6px;margin-bottom: 10px" type="primary"  @click="dialogFormVisible = true"> <i class="el-icon-plus"></i> Ajouter un employer</el-button>
       
	<el-table
            :data="staffs.slice((currentPage-1)*pagesize, currentPage*pagesize).filter(data => !search || data.firstName.toLowerCase().includes(search.toLowerCase()))" class="shadow-table">
            
            <el-table-column
              label="Prénom"
              prop="firstName"
             >
            </el-table-column>

          <el-table-column
              label="Nom"
              prop="lastName"
             >
            </el-table-column>
            <el-table-column
              label="Sexe"
              prop="gender"
             >
            </el-table-column>
            <el-table-column
              label="Date de naissance"
              prop="dateOfBirth"
            >
              <template slot-scope="scope">
                {{ toReadable(scope.row.dateOfBirth) }}
              </template>
            </el-table-column>

            <el-table-column
              label="Date de création"
              prop="creationDate"
            >
              <template slot-scope="scope">
                {{ toReadable(scope.row.creationDate) }}
              </template>
            </el-table-column>

            <el-table-column
              align="right">
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="search"
                  placeholder="Trouver un employée .."/>
              </template>
              <template scope="scope">
                <el-tooltip class="item" content="Modifier cet cadre" placement="top-start">
                  <el-button
                    type="success"
                    icon="el-icon-edit"
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
            :total="staffs.length"
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
                modelStaff: {
               
                    firstName: '',
                    lastName: '',
                    gender: '',
                    dateOfBirth: '',
                    functions: [
					{function: 'Ensigenat'}, 
					{function: 'RH'}, 
					{function: 'Gardien'}, 
					{function: 'Coursier'}, 
					{function:'Menage'}
					
					], 
                },
                
                formLabelWidth: '120px',
                search: '',
                preview: null
          }


        },
        mounted() {
               this.$store.dispatch('staff/FETCH_STAFFS')
             

        },

        computed: {
            ...mapState(
                {  staffs: state => state.staff.staffs
                }
                    
            ),
     
            ...mapGetters(['staff/STAFF']),


        },
        methods: {
            ...mapActions({
                add: "staff/ADD_STAFF",
                delete:"staff/DELETE_STAFF",
                update:"staff/UPDATE_STAFF"},
            ),
            addStaff(modelStaff) {
               
                    this.$refs[modelStaff].validate((valid) => {
                        if (valid) {

                            try {
                                this.$store.dispatch('staff/ADD_STAFF', {

                                    payload: this.modelStaff


                                }).then(() => {

                                    this.$notify({
                                        title: 'Success',
                                        message: 'Un emplyée est ajouté avec succès !',
                                        type: 'success'
                                    });
                                    this.resetForm(modelStaff)
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
            updateStaff(editStaff){
                
                    this.$refs[editStaff].validate((valid) => {
                        if (valid) {
                            this.update(this.editStaff)
                            this.$notify({
                                title: 'Succès ',
                                message: 'employer  modifée avec succés!',
                                type: 'success'
                            });
                            this.dialogFormUpdateVisible = false

                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    });
            },

            deleteStaff(){
                this.delete(this.formModelStaff)
                this.$message({
                    message: 'employee supprimé avec avec succès !',
                    type: 'error'
                });         this.dialogDeleteVisible = false
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();

            },
            handleEdit(index, row) {
                console.log(index, row);
                this.dialogFormUpdateVisible = true;
                this.editStaff = Object.assign({}, row);
            },
            handleRead(index, row) {
                console.log(index, row);
                this.dialogVisible = true;
                this.editStaff = Object.assign({}, row);
            },
            handleDelete(index, row) {
                this.formModelStaff=Object.assign({}, row);
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