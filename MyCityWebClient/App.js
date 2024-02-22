
import './styles/App.css';
import { Route, Routes, useLocation} from "react-router-dom";
import Login from "./pages/login";
import Users from "./components/Users"
import Institutions from "./components/Institutions"
import Facilities from "./components/Facilities"
import Events from "./components/Events"
import News from "./components/News"
import Alerts from "./components/Alerts"
import Issues from "./components/Issues"
import Dashboard from "./components/Dashboard"
import Issue from "./components/Issue"
import Appbar from "./components/Appbar"
import Sidebar from "./components/Sidebar"
import InstitutionIssues from "./components/IntitutionIssues"
import InstitutionSidebar from "./components/InstitutionSidebar";
import InstitutionNews from "./components/InstitutionNews"
import InstitutionAlerts from "./components/InstitutionAlerts"
import InstitutionDashboard from "./components/InstitutionDashboard"
import { LoadScript } from '@react-google-maps/api';
function App() {

    const location = useLocation();

  return (
        <div className="app" >
            <main className="appContent">
                <LoadScript googleMapsApiKey="AIzaSyBHQhTGAvSM7sqxVg65DxIiZOW5f5QnyqQ"  libraries={[]}></LoadScript>
                {(location.pathname.startsWith("/admin") || location.pathname.startsWith("/institution")) && <Appbar />}
                {location.pathname.startsWith("/admin") && <Sidebar />}
                {location.pathname.startsWith("/institution") && <InstitutionSidebar />}
                <Routes>
                    <Route exact path="/" element={<Login/>} />
                    <Route exact path="/admin" element={<Dashboard/>} />
                    <Route path="/admin/users" element={<Users />} />
                    <Route path="/admin/institutions" element={<Institutions />} />
                    <Route path="/admin/facilities" element={<Facilities />} />
                    <Route path="/admin/events" element={<Events />} />
                    <Route path="/admin/issues" element={<Issues />} />
                    <Route path="/admin/news" element={<News />} />
                    <Route path="/admin/alerts" element={<Alerts />} />
                    <Route exact path="/institution" element={<InstitutionDashboard/>} />
                    <Route path="/institution/issues" element={<InstitutionIssues/>} />
                    <Route path="/institution/news" element={<InstitutionNews />} />
                    <Route path="/institution/issue/:id" element={<Issue />} />
                    <Route path="/institution/alerts" element={<InstitutionAlerts />} />
                </Routes>
            </main>

        </div>


  );
}

export default App;


