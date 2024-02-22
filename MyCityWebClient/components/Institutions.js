import React, {useState, useEffect} from "react";
import {
    Box,
    Typography,
    Dialog,
    DialogTitle,
    DialogContent,
    TextField,
    DialogActions,
    Button,
    InputLabel,
    Input
} from '@mui/material'
import { DataGrid } from "@mui/x-data-grid";
import {useNavigate} from "react-router-dom";
import IconButton from '@mui/material/IconButton';
import {Delete,Edit, Add} from '@mui/icons-material/';
import {institutions} from "../services/institutions";
import {fileUploader} from "../services/fileUploader";


const Institutions = () =>{

    const user = JSON.parse(sessionStorage.getItem('user'));
    const [refreshKey, setRefreshKey] = useState(0);
    const [selectedInstitution, setSelectedInstitution] = useState([])
    const [openDialog, setOpenDialog] = useState(false)
    const [dialogTitle,setDialogTitle]=useState("")
    const [formName,setFormName]=useState("")
    const [formPhone,setFormPhone]=useState("")
    const [formEmail,setFormEmail]=useState("")
    const [formAddress,setFormAddress]=useState("")
    const [formWebsite,setFormWebsite]=useState("")
    const [isFormValid,setIsFormValid]=useState(true)

    const [nameHelper, setNameHelper]=useState("")
    const [addressHelper, setAddressHelper]=useState("")
    const [emailHelper, setEmailHelper]=useState("")
    const [phoneHelper, setPhoneHelper]=useState("")
    const [websiteHelper, setWebsiteHelper]=useState("")

    const [addressError,setAddressError]=useState(false)
    const [nameError,setNameError]=useState(false)
    const [emailError,setEmailError]=useState(false)
    const [phoneError,setPhoneError]=useState(false)
    const [websiteError,setWebsiteError]=useState(false)

    const [allInstitutions,setAllInstitutions]=useState([])

    const [formImageFile, setFormImageFile]=useState(null)
    const [formImageName, setFormImageName]=useState("")

    const [openConfirmDialog, setOpenConfirmDialog]=useState(false)

    const navigate = useNavigate();


    const columns=[
        {
            field: "id",
            headerName: "ID",
            flex:0.02
        },
        {
            field: "name",
            headerName: "Name",
            flex:0.21
        },

        {
            field: "address",
            headerName: "Address",
            flex:0.22
        },
        {
            field: "email",
            headerName: "Email",
            flex:0.225
        },
        {
            field: "phoneNumber",
            headerName: "Phone Number",
            flex:0.1
        },
        {
            field: "website",
            headerName: "Website",
            flex:0.2
        },
      {
            field: 'actions',
            headerName: 'Actions',
            flex:0.08,
            sortable: false,
            renderCell: (params) => (
                <>
                    <IconButton aria-label="edit" sx={{pr:0}}  onClick={() => {
                        updateInstitution(params.row)}
                    }>
                        <Edit />
                    </IconButton>
                    <IconButton aria-label="delete"  onClick={() => {
                        setOpenConfirmDialog(true)
                        setSelectedInstitution(params.row)
                    }}>
                        <Delete />
                    </IconButton>
                </>
            ),
            params: (params) => ({ ...params, row: params.row }),
        },
    ]

    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function getAllInstitutions(){
            const res= await institutions.findAll();
            setAllInstitutions(res);
        }

        getAllInstitutions();
    }, [refreshKey])



    const confirmDeleteInstitution =(id)=>{
        institutions.delete(id).then(()=> {
            setRefreshKey(refreshKey + 1)
            setOpenConfirmDialog(false)
        })
    }

    const updateInstitution=(institution)=>{
        setDialogTitle("Update Institution")
        setFormName(institution.name)
        setFormAddress(institution.address)
        setFormWebsite(institution.website)
        setFormPhone(institution.phoneNumber)
        setFormEmail(institution.email)
        setFormImageName(institution.image)
        setOpenDialog(true)
    }


    const createInstitution = () =>{
        setDialogTitle("Create Institution")
        setOpenDialog(true)
    }
    const cancel = () =>{
        setOpenDialog(false)
        setFormName("")
        setFormWebsite("")
        setFormAddress("")
        setFormEmail("")
        setFormPhone("")
        setIsFormValid(true)
        setAddressHelper('')
        setEmailHelper('')
        setPhoneHelper('')
        setWebsiteHelper('')
        setNameHelper('')
        setNameError(false)
        setAddressError(false)
        setPhoneError(false)
        setEmailError(false)
        setWebsiteError(false)
        setFormImageName("")
    }

    const validForm = () => {
        return !nameError&&!addressError&&!emailError&&!phoneError&&!websiteError&&formName!==''&&formAddress!==''&&formEmail!==''&&formPhone!==''&&formWebsite!==''&&formImageName!==''

    }
    const submitAddInstitution=(institution)=>{
        const file={
            preview: URL.createObjectURL(formImageFile),
            data: formImageFile,
        }
        fileUploader.handleSubmit(file).then(publicUrl => {
            institution.image=publicUrl
            institutions.create(institution).then((response)=>{
                setRefreshKey(refreshKey + 1)
            })
                .catch(()=>alert("Error creating institution"));
        })


    }

    const submitUpdateInstitution=(institution)=>{
        institution.id=selectedInstitution.id
        const file={
            preview: URL.createObjectURL(formImageFile),
            data: formImageFile,
        }
        fileUploader.handleSubmit(file).then(publicUrl => {
            institution.image=publicUrl
            institutions.update(institution).then((response)=>{
                setRefreshKey(refreshKey + 1)
            })
                .catch(()=>alert("Error updating institution"));
        })
    }
    const submit = () =>{
        let institution={
            id: null,
            name: formName,
            address: formAddress,
            phoneNumber: formPhone,
            email: formEmail,
            website:formWebsite
        };

        if(validForm()){
            setIsFormValid(true)
            if(dialogTitle==="Create Institution"){
                submitAddInstitution(institution);
            }else{
                submitUpdateInstitution(institution);
            }
            cancel();
            setOpenDialog(false);
        }
        else{
            setIsFormValid(false)
        }
    }

    const handleNameBlur=()=>{
        let helperText=''
        let error=false
        if(formName.length<1){
            helperText="Name cannot be empty"
            error=true
        }
        setNameHelper(helperText)
        setNameError(error)
    }
    const handleAddressBlur=()=>{
        let helperText=''
        let error=false
        if(formName.length<1){
            helperText="Address cannot be empty"
            error=true
        }
        setAddressHelper(helperText)
        setAddressError(error)
    }
    const handlePhoneBlur=()=>{
        let helperText=''
        let error=false
        const phoneRegex=/^(\+4|)?(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?(\s|\.|\-)?([0-9]{3}(\s|\.|\-|)){2}$/
        if(allInstitutions.find(institution => institution.phone === formPhone) && !(dialogTitle==="Update Institution" && formPhone===selectedInstitution.phone)){
            helperText="Phone number already exists"
            error=true
        }
        if(!phoneRegex.test(formPhone)){
            helperText="Phone number is not valid"
            error=true
        }
        setPhoneError(error)
        setPhoneHelper(helperText)
    }
    const handleWebsiteBlur=()=>{
        let helperText=''
        let error=false
        const websiteRegex=/^https?:\/\/(?:www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b(?:[-a-zA-Z0-9()@:%_\+.~#?&\/=]*)$/
        if(allInstitutions.find(institution => institution.website === formWebsite) && !(dialogTitle==="Update Institution" && formWebsite===selectedInstitution.website)){
            helperText="Website link already exists"
            error=true
        }
        if(!websiteRegex.test(formWebsite)){
            helperText="Website link is not valid"
            error=true
        }
        setWebsiteError(error)
        setWebsiteHelper(helperText)
    }
    const handleEmailBlur=()=>{
        let helperText=''
        let error=false
        const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
        if(allInstitutions.find(institution => institution.email === formEmail) && !(dialogTitle==="Update Institution" && formEmail===selectedInstitution.email)){
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


    return (
        user &&<Box mt="90px" ml="290px">
            <Box mb="20px">
                <Typography
                    variant="h5"
                    fontWeight="bold"
                    color="#192841"
                >
                    Institutions
                </Typography>
                <Typography variant="body1" color="text.secondary"  >
                    Manage Institution Information
                </Typography>
                <Box sx={{display: 'flex', flexDirection:'row', justifyContent:'right', width:"97%"}}>
                    <Button variant="contained" onClick={()=>createInstitution()}>
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
                <DataGrid rows={allInstitutions}
                          columns={columns}
                          pageSize={10}
                          rowsPerPageOptions={[10]}
                          onRowClick={(rowInfo) =>
                          { setSelectedInstitution(rowInfo.row)}}/>
            </Box>


            <Dialog open={openDialog} >
                <DialogTitle sx={{ml:1}}>{dialogTitle}</DialogTitle>
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
                        id="address"
                        label="Address"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={addressError}
                        onBlur={handleAddressBlur}
                        helperText={addressHelper}
                        value={formAddress}
                        onChange={(e) => setFormAddress(e.target.value)}
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
                    <TextField
                        margin="dense"
                        id="phone"
                        label="Phone"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={phoneError}
                        onBlur={handlePhoneBlur}
                        helperText={phoneHelper}
                        value={formPhone}
                        onChange={(e) => setFormPhone(e.target.value)}
                    />
                    <TextField
                        margin="dense"
                        id="website"
                        label="Website"
                        type="text"
                        fullWidth
                        variant="outlined"
                        size="small"
                        error={websiteError}
                        onBlur={handleWebsiteBlur}
                        helperText={websiteHelper}
                        value={formWebsite}
                        onChange={(e) => setFormWebsite(e.target.value)}
                    />
                    <Box  border={1} borderRadius={1} borderColor="#aaaaaa" height="40px" mt="10px">
                        <InputLabel htmlFor="upload-file"  sx={{pl:"15px", pt:"8px"}}>
                            {formImageName==="" ? "Upload Picture" : formImageName}
                        </InputLabel>
                        <Input
                            id="upload-file"
                            type="file"
                            inputProps={{ style: { display: "none" } }}
                            onChange={e=>{setFormImageFile(e.target.files[0]);setFormImageName(e.target.files[0].name)}}
                        />

                    </Box>

                    <Typography color="red">{isFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>

                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={cancel}>Cancel</Button>
                    <Button variant="contained" onClick={submit}>Submit</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={openConfirmDialog} >
                <DialogTitle>Delete institution?</DialogTitle>
                <DialogContent>
                    Once you confirm this action, the institution will be permanently deleted and will no longer be visible in the application
                </DialogContent>
                <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                    <Button variant="contained" onClick={()=>setOpenConfirmDialog(false)}>Cancel</Button>
                    <Button variant="contained" onClick={confirmDeleteInstitution}>Delete</Button>
                </DialogActions>
            </Dialog>
        </Box>
    )
}

export default Institutions;