import React, {useState, useEffect} from "react";
import {Box,Typography, Dialog, DialogTitle, DialogContent, TextField, DialogActions, Button, FormControl,InputLabel, Select,MenuItem} from '@mui/material'
import { DataGrid } from "@mui/x-data-grid";
import IconButton from '@mui/material/IconButton';
import {Delete,Edit, Add} from '@mui/icons-material/';
import {users} from "../services/users";
import {institutions} from "../services/institutions";
import {useNavigate} from "react-router-dom";

const Users = () =>{

    const user = JSON.parse(sessionStorage.getItem('user'));
    const [refreshKey, setRefreshKey] = useState(0);
    const [openUserDialog, setOpenUserDialog]=useState(false);
    const [userDialogTitle, setUserDialogTitle]=useState("");
    const [formUsername, setFormUsername]=useState("");
    const [formName, setFormName]=useState("");
    const [formEmail, setFormEmail]=useState("");
    const [formPassword, setFormPassword]=useState("");
    const [formConfirmPassword, setFormConfirmPassword]=useState("");
    const [selectedUser, setSelectedUser]=useState([]);
    const [selectedRole, setSelectedRole]=useState("")
    const [allUsers,setAllUsers]=useState([])
    const [isFormValid, setIsFormValid]=useState(true)

    const [selectedInstitution,setSelectedInstitution]=useState([])

    const [nameHelper, setNameHelper]=useState("")
    const [usernameHelper, setUsernameHelper]=useState("")
    const [emailHelper, setEmailHelper]=useState("")
    const [passwordHelper, setPasswordHelper]=useState("")
    const [confirmPasswordHelper, setConfirmPasswordHelper]=useState("")

    const [usernameError,setUsernameError]=useState(false)
    const [nameError,setNameError]=useState(false)
    const [emailError,setEmailError]=useState(false)
    const [passwordError,setPasswordError]=useState(false)
    const [confirmPasswordError,setConfirmPasswordError]=useState(false)

    const [allInstitutions,setAllInstitutions]=useState([])


    const navigate = useNavigate();

    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function getAllUsers(){
            const res= await users.findAll();
            setAllUsers(res);
        }

        async function getAllInstitutions(){
            const res= await institutions.findAll();
            setAllInstitutions(res);
        }

        getAllUsers();
        getAllInstitutions();
    }, [refreshKey])

    const editUser = (user) =>{
        setSelectedUser(user)
        setUserDialogTitle("Edit User")
        setFormName(user.name)
        setFormUsername(user.username)
        setFormEmail(user.email)
        setSelectedRole(user.role)
        setOpenUserDialog(true)
    }

    const deleteUser = (id) =>{
        users.delete(id).then(()=> setRefreshKey(refreshKey + 1));
    }

    const cancel = () =>{
        setOpenUserDialog(false)
        setFormName("")
        setFormUsername("")
        setFormPassword("")
        setFormEmail("")
        setFormConfirmPassword("")
        setSelectedRole("")
        setIsFormValid(true)
        setNameError(false)
        setUsernameError(false)
        setPasswordError(false)
        setConfirmPasswordError(false)
        setEmailError(false)
        setNameHelper('')
        setUsernameHelper('')
        setEmailHelper('')
        setPasswordHelper('')
        setConfirmPasswordHelper('')
        setSelectedInstitution('')
    }


    const validForm = () => {
        if(userDialogTitle==='Create User'){
            return !nameError&&!usernameError&&!passwordError&&!confirmPasswordError&&!emailError&&formName!==''&&formUsername!==''&&formPassword!==''&&formConfirmPassword!==''&&formEmail!==''&&selectedRole!==''
        }
        return !nameError&&!usernameError&&!emailError&&formName!==''&&formUsername!==''&&formEmail!==''&&selectedRole!==''

    }

    const submitAddUser = (formUser) => {
        users.create(formUser).then((response)=>{
            setRefreshKey(refreshKey + 1)
        })
            .catch(()=>alert("Error creating user"));
    }

    const submitUpdateUser = (formUser) => {
        formUser.id=selectedUser.id
        users.update(formUser).then((response)=>{
            setRefreshKey(refreshKey + 1)
        })
            .catch(()=>alert("Error updating user"));
    }

    const submitUser = () =>{
        let formUser={
            id: null,
            username: formUsername,
            name: formName,
            email: formEmail,
            password: formPassword,
            role: selectedRole
        };

        if(validForm()){
            setIsFormValid(true)
            if(userDialogTitle==="Create User"){
                submitAddUser(formUser);
            }else{
                submitUpdateUser(formUser);
            }
            cancel();
            setOpenUserDialog(false);
        }
        else{
            setIsFormValid(false)
        }
    }

    const createUser = async () =>{
        await cancel()
        await setUserDialogTitle("Create User")
        setOpenUserDialog(true)
    }


    const handleNameBlur =  async () =>{
        let helperText=''
        let error=false
        if(formName.length<1){
            helperText="Name cannot be empty"
            error=true
        }
        setNameHelper(helperText)
        setNameError(error)
    }

    const handleUsernameBlur =  async () =>{
        let helperText=''
        let error=false
        if(formUsername.length<1){
            helperText="Username cannot be empty"
            error=true
        }
        if(allUsers.find(user => user.username === formUsername) && !(userDialogTitle==="Edit User" && formUsername===selectedUser.username)){
            helperText="Username already exists"
            error=true
        }
        setUsernameHelper(helperText)
        setUsernameError(error)
    }

    const handleEmailBlur =  async () =>{
        let helperText=''
        let error=false
        const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
        if(allUsers.find(user => user.email === formEmail) && !(userDialogTitle==="Edit User" && formEmail===selectedUser.email)){
            helperText="Email already exists"
            error=true
        }
        if(!emailRegex.test(formEmail)){
            helperText="Email is not valid"
            error=true
        }
        setEmailError(error)
        setEmailHelper(helperText)
    }

    const handlePasswordBlur= async () =>{
        let helperText=''
        let error=false
        if(formPassword.length<6){
            helperText="Password must be at least 6 characters long"
            error=true
        }
        setPasswordError(error)
        setPasswordHelper(helperText)
    }

    const handleConfirmPasswordBlur=async () =>{
        let helperText=''
        let error=false
        if(formPassword !==  formConfirmPassword){
            helperText="Passwords don't match"
            error=true
        }
        setConfirmPasswordHelper(helperText)
        setConfirmPasswordError(error)
    }


    const columns = [
        {
            field: "id",
            headerName: "ID"
        },
        {
            field: "name",
            headerName: "Name",
            flex: 1,
        },
        {
            field: "username",
            headerName: "Username",
            flex: 1,
        },
        {
            field: "email",
            headerName: "Email",
            flex: 1,
        },
        {
            field: "role",
            headerName: "Role",
            flex: 1,
        },{
            field: 'actions',
            headerName: 'Actions',
            width: 150,
            sortable: false,
            renderCell: (params) => (
                <>
                    <IconButton aria-label="edit"  onClick={() => {
                        editUser(params.row)}
                    }>
                        <Edit />
                    </IconButton>
                    <IconButton aria-label="delete"  onClick={() => deleteUser(params.row.id)}>
                        <Delete />
                    </IconButton>
                </>
            ),
            params: (params) => ({ ...params, row: params.row }),
        }]


    return (
        user &&<Box mt="90px" ml="290px">
            <Box mb="20px">
                <Typography
                    variant="h5"
                    fontWeight="bold"
                    color="#192841"
                >
                    Users
                </Typography>
                <Typography variant="body1" color="text.secondary" >
                    Manage Application Users
                </Typography>
                <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'right', width:"97%"}}>
                    <Button variant="contained" onClick={()=>createUser()}>
                        <Add /> Create
                    </Button>
                </Box>


            </Box>

            <Box height="475px" width="97%"
                 sx={{
                     "& .MuiDataGrid-root": {
                         border: "1px solid #aaaaaa !important",
                     },
                     "& .MuiDataGrid-cell": {
                         borderBottom: "1px solid #bbbbbb !important",

                     },
                     "& .MuiDataGrid-columnHeaders": {
                         borderBottom: "1px solid #aaaaaa !important",
                         // backgroundColor: "#cccccc",
                     },
                 }}
                >
                <DataGrid rows={allUsers}
                          columns={columns}
                          pageSize={10}
                          rowsPerPageOptions={[10]}
                          onRowClick={(rowInfo) =>
                          { setSelectedUser(rowInfo.row)}}/>
            </Box>


            <Dialog open={openUserDialog} >
                <DialogTitle sx={{ml:1}}>{userDialogTitle}</DialogTitle>
                <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"400px",p:"0 35px 10px 35px"}}>
                    <TextField
                        margin="dense"
                        id="name"
                        label="Name"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={nameError}
                        onBlur={handleNameBlur}
                        helperText={nameHelper}

                        value={formName}
                        onChange={(e) => setFormName(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="username"
                        label="Username"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        value={formUsername}
                        onChange={(e) => setFormUsername(e.target.value)}
                        error={usernameError}
                        onBlur={handleUsernameBlur}
                        helperText={usernameHelper}
                    />

                    <TextField
                        margin="dense"
                        id="email"
                        label="Email"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={emailError}
                        onBlur={handleEmailBlur}
                        helperText={emailHelper}
                        value={formEmail}
                        onChange={(e) => setFormEmail(e.target.value)}
                    />
                    {userDialogTitle==="Create User" ? <TextField
                        margin="dense"
                        id="password"
                        label="Password"
                        type="password"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={passwordError}
                        onBlur={handlePasswordBlur}
                        helperText={passwordHelper}
                        value={formPassword}
                        onChange={(e) => setFormPassword(e.target.value)}
                    /> :null}
                    {userDialogTitle==="Create User" ? <TextField
                        margin="dense"
                        id="confirmPassword"
                        label="Confirm Password"
                        type="password"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={confirmPasswordError}
                        onBlur={handleConfirmPasswordBlur}
                        helperText={confirmPasswordHelper}
                        value={formConfirmPassword}
                        onChange={(e) => setFormConfirmPassword(e.target.value)}
                    /> :null}

                    {userDialogTitle==="Create User" ? <FormControl fullWidth margin="dense" size="small">
                        <InputLabel id="selectRole">Role</InputLabel>
                        <Select
                            labelId="selectRole"
                            value={selectedRole}
                            label="Role"
                            onChange={(e) => setSelectedRole(e.target.value)}
                        >
                            <MenuItem value={"Admin"}>Admin</MenuItem>
                            <MenuItem value={"Regular User"}>Regular User</MenuItem>
                            <MenuItem value={"Institution User"}>Institution User</MenuItem>
                        </Select>
                    </FormControl>:null}

                    {userDialogTitle==="Create User" && selectedRole==="Institution User" ? <FormControl fullWidth margin="dense" size="small">
                        <InputLabel id="selectInstitution">Institution</InputLabel>
                        <Select
                            labelId="selectInstitution"
                            value={selectedInstitution}
                            label="Institution"
                            onChange={(e) => setSelectedInstitution(e.target.value)}
                        >
                            {allInstitutions.map(
                                (institution) => {
                                    return (
                                        <MenuItem key={institution.id} value={institution}>{institution.name}</MenuItem>
                                    )
                                }
                            )}

                        </Select>
                    </FormControl>:null}

                    <Typography color="red">{isFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>

                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={cancel}>Cancel</Button>
                    <Button variant="contained" onClick={submitUser}>Submit</Button>
                </DialogActions>
            </Dialog>
        </Box>
    )
}

export default Users;