import React, {useEffect, useState} from "react";
import {
    Box,
    Button,
    Dialog, DialogActions, DialogContent, DialogTitle,
    List,
    ListItem,
    TextField,
    Typography
} from '@mui/material'
import {useNavigate} from "react-router-dom";
import 'chart.js/auto';
import {analytics} from "../services/analytics";
import {Bar, Doughnut} from "react-chartjs-2";
import {institutions} from "../services/institutions";
import {Email, InsertLink, LocationOn, Phone} from "@mui/icons-material";
import {users} from "../services/users";
const InstitutionDashboard = () =>{

    const user = JSON.parse(sessionStorage.getItem('user'));
    const navigate=useNavigate()

    const [institution,setInstitution]=useState([])

    const [issues,setIssues]=useState(0)
    const [news,setNews]=useState(0)
    const [alerts,setAlerts]=useState(0)
    const [issueStatuses,setIssueStatuses]=useState([])
    const [issueTypes,setIssueTypes]=useState([])

    const [openUserDialog, setOpenUserDialog]=useState(false);
    const [openInstitutionDialog, setOpenInstitutionDialog]=useState(false);
    const [openConfirmationDialog, setOpenConfirmationDialog]=useState(false);

    const [formUsername, setFormUsername]=useState("");
    const [formName, setFormName]=useState("");
    const [formEmail, setFormEmail]=useState("");
    const [formPassword, setFormPassword]=useState("");
    const [formConfirmPassword, setFormConfirmPassword]=useState("");

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

    const [isFormValid, setIsFormValid]=useState(true)
    const [confirmationDialogTitle,setConfirmationDialogTitle]=useState("")

    const [addressHelper, setAddressHelper]=useState("")
    const [institutionEmailHelper, setInstitutionEmailHelper]=useState("")
    const [phoneHelper, setPhoneHelper]=useState("")
    const [websiteHelper, setWebsiteHelper]=useState("")

    const [addressError,setAddressError]=useState(false)
    const [institutionEmailError,setInstitutionEmailError]=useState(false)
    const [phoneError,setPhoneError]=useState(false)
    const [websiteError,setWebsiteError]=useState(false)

    const [formPhone,setFormPhone]=useState("")
    const [formInstitutionEmail,setFormInstitutionEmail]=useState("")
    const [formAddress,setFormAddress]=useState("")
    const [formWebsite,setFormWebsite]=useState("")
    const [isInstitutionFormValid,setIsInstitutionFormValid]=useState(true)

    const [refreshKey,setRefreshKey]=useState(0)


    const issuesStatusData={
        labels:  issueStatuses.map((issueStatus)=>issueStatus.status),
        datasets: [
            {
                data: issueStatuses.map((issueStatus)=>issueStatus.count),
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
                hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
            },
        ],
    }


    const issueTypeData ={
        labels: issueTypes.map((issueType)=>issueType.type),

        datasets: [{
            label: "Number of issues",
            backgroundColor: '#FF6384',
            border:1,
            borderColor: 'rgba(0,0,0,1)',
            data: issueTypes.map((issueType)=>issueType.count),


        }]
    }




    useEffect(() => {
        if(!user){
            navigate("/")
        }
        async function init(){
            const institution= await institutions.findByUserId(user.id);
            setInstitution(institution)
            sessionStorage.setItem("institution", JSON.stringify(institution))

            const result= await analytics.getInstitutionAnalytics(institution.id,institution.name);
            setIssues(result.issues)
            setNews(result.news)
            setAlerts(result.alerts)
            setIssueStatuses(result.issueStatuses)
            setIssueTypes(result.issueTypes)
        }

        init()
    }, [refreshKey])




    const openCreateAccount=()=>{
        setOpenUserDialog(true)
    }


    const cancel=()=>{
        setOpenUserDialog(false)
        setFormName("")
        setFormUsername("")
        setFormPassword("")
        setFormEmail("")
        setFormConfirmPassword("")
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
    }



    const validForm = () => {
        return !nameError&&!usernameError&&!passwordError&&!confirmPasswordError&&!emailError&&formName!==''&&formUsername!==''&&formPassword!==''&&formConfirmPassword!==''&&formEmail!==''
    }

    const submitAddUser = (formUser) => {
        users.createInstitutionUser(formUser,institution.name).then((response)=>{
            cancel();
            setOpenUserDialog(false);
            setOpenConfirmationDialog(true)
            setConfirmationDialogTitle("New account created successfully!")
        })
            .catch(()=>alert("Error creating user"));
    }



    const submitUser = () =>{
        let formUser={
            id: null,
            username: formUsername,
            name: formName,
            email: formEmail,
            password: formPassword,
            role: "Institution User"
        };

        if(validForm()){
            setIsFormValid(true)
            submitAddUser(formUser);

        }
        else{
            setIsFormValid(false)
        }
    }




    const handleNameBlur =  () =>{
        let helperText=''
        let error=false
        if(formName.length<1){
            helperText="Name cannot be empty"
            error=true
        }
        setNameHelper(helperText)
        setNameError(error)
    }

    const handleUsernameBlur = async () =>{
        let helperText=''
        let error=false
        if(formUsername.length<1){
            helperText="Username cannot be empty"
            error=true
        }else{
            await users.existsUsername(formUsername).then((response)=>{
                if(response===true){
                    helperText="Username already exists"
                    error=true
                }

            })
        }
        setUsernameHelper(helperText)
        setUsernameError(error)


    }

    const handleEmailBlur = async () =>{
        let helperText=''
        let error=false
        if(formEmail.length<1){
            helperText="Email cannot be empty"
            error=true
        }else{
            const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
            if(!emailRegex.test(formEmail)){
                helperText="Email is not valid"
                error=true
            }else{
                await users.existsEmail(formEmail).then((response)=>{
                    if(response===true){
                        helperText="Email already exists"
                        error=true
                    }

                })
            }
        }

        setEmailError(error)
        setEmailHelper(helperText)

    }

    const handlePasswordBlur= () =>{
        let helperText=''
        let error=false
        if(formPassword.length<6){
            helperText="Password must be at least 6 characters long"
            error=true
        }
        setPasswordError(error)
        setPasswordHelper(helperText)
    }

    const handleConfirmPasswordBlur= () =>{
        let helperText=''
        let error=false
        if(formPassword !==  formConfirmPassword){
            helperText="Passwords don't match"
            error=true
        }
        setConfirmPasswordHelper(helperText)
        setConfirmPasswordError(error)
    }

    const closeSuccessDialog=()=>{
        setOpenConfirmationDialog(false)
    }



    const openEditInstitution=()=>{

        setFormAddress(institution.address)
        setFormWebsite(institution.website)
        setFormPhone(institution.phoneNumber)
        setFormInstitutionEmail(institution.email)
        setOpenInstitutionDialog(true)
    }

    const cancelInstitution=()=>{
        setFormWebsite("")
        setFormAddress("")
        setFormInstitutionEmail("")
        setFormPhone("")
        setAddressHelper('')
        setInstitutionEmailHelper('')
        setPhoneHelper('')
        setWebsiteHelper('')

        setAddressError(false)
        setPhoneError(false)
        setInstitutionEmailError(false)
        setWebsiteError(false)

        setIsInstitutionFormValid(true)
        setOpenInstitutionDialog(false)
    }

    const validInstitutionForm = () => {
        return !addressError&&!institutionEmailError&&!phoneError&&!websiteError&&formAddress!==''&&formInstitutionEmail!==''&&formPhone!==''&&formWebsite!==''
    }



    const submitInstitution=(updated)=>{
        institutions.update(updated).then(()=>{
            setRefreshKey(refreshKey + 1)
            cancelInstitution();
            setOpenInstitutionDialog(false);
            setConfirmationDialogTitle("Institution details updated successfully!")
            setOpenConfirmationDialog(true)
        })
            .catch(()=>alert("Error updating institution"));
        setOpenConfirmationDialog(false)
    }

    const handleSubmitInstitution=()=>{
        let updated={
            id: institution.id,
            name: institution.name,
            address: formAddress,
            phoneNumber: formPhone,
            email: formInstitutionEmail,
            website:formWebsite
        };

        if(validInstitutionForm()){
            setIsFormValid(true)
            submitInstitution(updated);

        }
        else{
            setIsInstitutionFormValid(false)
        }
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
        if(!websiteRegex.test(formWebsite)){
            helperText="Website link is not valid"
            error=true
        }
        setWebsiteError(error)
        setWebsiteHelper(helperText)
    }
    const handleInstitutionEmailBlur=  ()=>{
        let helperText=''
        let error=false
        const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
        if(!emailRegex.test(formInstitutionEmail)){
            helperText="Email is not valid"
            error=true
        }


        setInstitutionEmailError(error)
        setInstitutionEmailHelper(helperText)
    }







    return (
        user &&<div style={{backgroundColor:"#eeeeee", minHeight: '100vh',paddingBottom:'20px'}}>
            <Box  mb="20px" ml="290px">
                <Box pt="80px" mb="10px">
                    <Typography
                        variant="h5"
                        fontWeight="bold"
                        color="#192841"
                    >
                        Dashboard
                    </Typography>
                </Box>

                <Box sx={{display:"flex",flexDirection:"row",mb:'-35px'}}>
                    <Box sx={{display:"flex",flexDirection:"column", width:"50%"}}>
                        <Box sx={{display:"flex",flexDirection:"row"}}>
                            <Box sx={{display:"flex",flexDirection:"column", width:"48%",mr:"10px"}}>
                                <Box sx={{ border:1,mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"85px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                    <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                        {issues}
                                    </Typography>
                                    <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                        Issues Reported
                                    </Typography>
                                </Box>
                                <Box sx={{ border:1,mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"85px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                    <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                        {news}
                                    </Typography>
                                    <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                        News Posted
                                    </Typography>
                                </Box>
                                <Box sx={{ border:1,mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"85px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                    <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                        {alerts}
                                    </Typography>
                                    <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                        Alerts Sent
                                    </Typography>
                                </Box>

                            </Box>

                            <Box sx={{ width:"48%", border:1,mr:"10px",mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex', flexDirection: 'column', height:"280px",backgroundColor:"#ffffff"}}>
                                <Typography variant="h6"  sx={{ml:2,mt:1}}>
                                    Issues by status
                                </Typography>


                                <Box sx={{position: "relative", height:"230px", width:"230px",ml:2}}>
                                    <Doughnut
                                        data={issuesStatusData}

                                    />
                                </Box>
                            </Box>
                        </Box>

                        <Box sx={{ width:"98%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex', flexDirection: 'column', height:"290px",backgroundColor:"#ffffff"}}>
                            <Typography variant="h6" sx={{ml:2,mt:1}}>
                                Issues by type
                            </Typography>


                            <Box sx={{position: "relative", height:"350px",width:"490px",ml:"20px"}}>
                                <Bar
                                    data={issueTypeData}
                                />
                            </Box>
                        </Box>

                    </Box>

                    <Box sx={{display:"flex",flexDirection:"column", width:"47%"}}>
                        <Box sx={{ border:1,mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"100%",backgroundColor:"#ffffff"}}>
                            <Box
                                borderRadius="10px 10px 0 0"
                                component="img"
                                sx={{
                                    width:"100%",
                                    height:"280px"
                                }}
                                alt="Image"
                                src={institution.image}
                            />

                            <Box   mt="20px">
                                <Typography variant="h5" fontWeight="bold"  color="#192841" alignContent="center" ml="20px" >
                                    {institution.name}
                                </Typography>
                                <List>
                                    <ListItem>
                                        <LocationOn sx={{mr:"5px"}}/>
                                        <Typography variant="body1"  color="text.primary" alignContent="center" >
                                            {institution.address}
                                        </Typography>
                                    </ListItem>
                                    <ListItem>
                                        <Email sx={{mr:"5px"}}/>
                                        <Typography variant="body1"  color="text.primary" alignContent="center" >
                                            {institution.email}
                                        </Typography>
                                    </ListItem>
                                    <ListItem>
                                        <Phone sx={{mr:"5px"}}/>
                                        <Typography variant="body1"  color="text.primary" alignContent="center" >
                                            {institution.phoneNumber}
                                        </Typography>
                                    </ListItem>
                                    <ListItem>
                                        <InsertLink sx={{mr:"5px"}}/>
                                        <Typography variant="body1"  color="text.primary" alignContent="center" >
                                            {institution.website}
                                        </Typography>
                                    </ListItem>

                                    <ListItem sx={{mt:"10px"}}>
                                        <Button variant="contained"  sx={{width:"300px",height:"40px", mr:"15px"}}  onClick={openEditInstitution}>
                                            Edit Institution Details
                                        </Button>
                                        <Button variant="contained"  sx={{width:"300px",height:"40px", mr:"15px"}}  onClick={openCreateAccount}>
                                            Create User Account
                                        </Button>
                                    </ListItem>
                                </List>


                            </Box>


                        </Box>
                    </Box>
                </Box>


                <Dialog open={openUserDialog} >
                    <DialogTitle sx={{ml:1}}>Create New Institution Account</DialogTitle>
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
                         <TextField
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
                        />
                         <TextField
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
                        />

                        <Typography color="red">{isFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>

                    </DialogContent>
                    <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                        <Button variant="contained" onClick={cancel}>Cancel</Button>
                        <Button variant="contained" onClick={submitUser}>Submit</Button>
                    </DialogActions>
                </Dialog>

                <Dialog open={openConfirmationDialog} >
                    <DialogTitle>{confirmationDialogTitle}</DialogTitle>
                    <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                        <Button variant="contained" onClick={closeSuccessDialog}>Close</Button>
                    </DialogActions>
                </Dialog>


                <Dialog open={openInstitutionDialog} >
                    <DialogTitle sx={{ml:1}}>Edit {institution.name}</DialogTitle>
                    <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: '10px', width:"400px",p:"0 35px 10px 35px"}}>
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
                            id="institution_email"
                            label="Email"
                            type="text"
                            fullWidth
                            variant="outlined"
                            size="small"
                            error={institutionEmailError}
                            onBlur={handleInstitutionEmailBlur}
                            helperText={institutionEmailHelper}
                            value={formInstitutionEmail}
                            onChange={(e) => setFormInstitutionEmail(e.target.value)}
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

                        <Typography color="red">{isInstitutionFormValid ? '' : 'Please make sure all fields are valid!'}</Typography>

                    </DialogContent>
                    <DialogActions  sx={{ p:"20px 35px 20px 35px"}}>
                        <Button variant="contained" onClick={cancelInstitution}>Cancel</Button>
                        <Button variant="contained" onClick={handleSubmitInstitution}>Submit</Button>
                    </DialogActions>
                </Dialog>


            </Box>
        </div>
    )
}

export default InstitutionDashboard;