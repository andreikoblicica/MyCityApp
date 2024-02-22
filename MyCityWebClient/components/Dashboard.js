import React, {useEffect, useState} from "react";
import {Box, Typography} from '@mui/material'
import {useNavigate} from "react-router-dom";
import 'chart.js/auto';
import {analytics} from "../services/analytics";
import {Bar, Doughnut} from "react-chartjs-2";
const Dashboard = () =>{

    const user = JSON.parse(sessionStorage.getItem('user'));
    const navigate=useNavigate()

    const [regularUsers,setRegularUsers]=useState(0)
    const [institutionUsers,setInstitutionUsers]=useState(0)
    const [institutions,setInstitutions]=useState(0)
    const [issues,setIssues]=useState(0)
    const [facilities,setFacilities]=useState(0)
    const [events,setEvents]=useState(0)
    const [news,setNews]=useState(0)
    const [alerts,setAlerts]=useState(0)
    const [issueStatuses,setIssueStatuses]=useState([])
    const [eventStatuses,setEventStatuses]=useState([])
    const [issueTypes,setIssueTypes]=useState([])
    const [eventTypes,setEventTypes]=useState([])

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

    const eventStatusData={
        labels: eventStatuses.map((eventStatus)=>eventStatus.status),
        datasets:
            [
            {
                data: eventStatuses.map((eventStatus)=>eventStatus.count),
                backgroundColor: ['#FF6384','#36A2EB', '#FFCE56'],
                hoverBackgroundColor: ['#FF6384', '#36A2EB','#FFCE56'],
            },
        ],
    }

    const issueTypeData ={
        labels: issueTypes.map((issueType)=>issueType.type),

        datasets: [{
            label: "Number of issues",
            backgroundColor: '#FFCE56',
            border:1,
            borderColor: 'rgba(0,0,0,1)',
            data: issueTypes.map((issueType)=>issueType.count),


        }]
    }

    const eventTypeData ={
        labels: eventTypes.map((eventType)=>eventType.type),

        datasets: [{
            label: "Number of events",
            backgroundColor: '#FF6384',
            border:1,
            borderColor: 'rgba(0,0,0,1)',
            data: eventTypes.map((eventType)=>eventType.count),

        }]
    }



    useEffect(() => {
        if(!user){
            navigate("/")
        }


        async function getAnalytics(){
            const result= await analytics.getAnalytics();
            setInstitutionUsers(result.institutionUsers)
            setRegularUsers(result.regularUsers)
            setInstitutions(result.institutions)
            setIssues(result.issues)
            setFacilities(result.facilities)
            setEvents(result.events)
            setNews(result.news)
            setAlerts(result.alerts)
            setIssueStatuses(result.issueStatuses)
            setEventStatuses(result.eventStatuses)
            setIssueTypes(result.issueTypes)
            setEventTypes(result.eventTypes)

        }

        getAnalytics();
    }, [])

    return (
        user &&<div style={{backgroundColor:"#eeeeee", minHeight: '100vh',paddingBottom:'20px'}}>
            <Box  mb="20px" ml="290px">
                <Box pt="85px" mb="10px">
                    <Typography
                        variant="h5"
                        fontWeight="bold"
                        color="#192841"
                    >
                        Dashboard
                    </Typography>
                </Box>

                <Box sx={{display:"flex",flexDirection:"row",mb:'-35px'}}>
                    <Box sx={{display:"flex",flexDirection:"column", width:"35%"}}>
                        <Box sx={{display:"flex",flexDirection:"row"}}>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"   color="#192841" alignContent="center">
                                    {institutionUsers}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Institution Users
                                </Typography>
                            </Box>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"   color="#192841" alignContent="center">
                                    {regularUsers}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Regular Users
                                </Typography>
                            </Box>
                        </Box>
                        <Box sx={{display:"flex",flexDirection:"row"}}>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"   color="#192841" alignContent="center">
                                    {institutions}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Institutions
                                </Typography>
                            </Box>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"   color="#192841" alignContent="center">
                                    {issues}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Issues Reported
                                </Typography>
                            </Box>
                        </Box>
                        <Box sx={{display:"flex",flexDirection:"row"}}>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                    {facilities}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Facilities
                                </Typography>
                            </Box>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                    {events}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Events Created
                                </Typography>
                            </Box>
                        </Box>
                        <Box sx={{display:"flex",flexDirection:"row"}}>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                    {news}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    News Posted
                                </Typography>
                            </Box>
                            <Box sx={{ width:"47%", border:1,mb:"10px",mr:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex',flexDirection:'column', height:"134px",backgroundColor:"#ffffff",alignItems:"center",justifyContent:"center"}}>
                                <Typography variant="h4" fontWeight="bold"  color="#192841" alignContent="center">
                                    {alerts}
                                </Typography>
                                <Typography variant="body1"  color="text.secondary" alignContent="center" >
                                    Alerts Sent
                                </Typography>
                            </Box>
                        </Box>
                    </Box>

                    <Box sx={{display:"flex",flexDirection:"column", width:"23%"}}>
                        <Box sx={{ width:"270px", border:1,mr:"20px",mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex', flexDirection: 'column', height:"280px",backgroundColor:"#ffffff"}}>
                            <Typography variant="h6"  sx={{ml:2,mt:1}}>
                                Issues by status
                            </Typography>


                            <Box sx={{position: "relative", height:"230px", width:"230px",ml:2}}>
                                <Doughnut
                                    data={issuesStatusData}

                                />
                            </Box>
                        </Box>
                        <Box sx={{ width:"270px", border:1,mr:"20px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex', flexDirection: 'column', height:"280px",backgroundColor:"#ffffff"}}>
                            <Typography variant="h6" sx={{ml:2,mt:1}}>
                                Events by status
                            </Typography>


                            <Box sx={{position: "relative", height:"230px", width:"230px",ml:2}}>
                                <Doughnut
                                    data={eventStatusData}

                                />
                            </Box>
                        </Box>
                    </Box>
                    <Box sx={{display:"flex",flexDirection:"column",width:"30%"}}>
                        <Box sx={{ width:"500px", border:1,mb:"10px", borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex', flexDirection: 'column', height:"280px",backgroundColor:"#ffffff"}}>
                            <Typography variant="h6" sx={{ml:2,mt:1}}>
                                Issues by type
                            </Typography>


                            <Box sx={{position: "relative", height:"350px",width:"450px",ml:"20px"}}>
                                <Bar
                                    data={issueTypeData}
                                />
                            </Box>
                        </Box>
                        <Box sx={{ width:"500px", border:1, borderColor:"#bbbbbb",borderRadius:"10px",display: 'flex', flexDirection: 'column', height:"280px",backgroundColor:"#ffffff"}}>
                            <Typography variant="h6" sx={{ml:2,mt:1}}>
                                Events by type
                            </Typography>


                            <Box sx={{position: "relative", height:"350px",width:"450px",ml:"20px"}}>
                                <Bar
                                    data={eventTypeData}
                                />
                            </Box>
                        </Box>
                    </Box>




                </Box>




            </Box>
    </div>
    )
}

export default Dashboard;