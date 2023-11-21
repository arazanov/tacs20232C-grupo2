import {useEffect, useState} from 'react';
import {
    Button,
    Card,
    CardBody,
    CardSubtitle,
    CardTitle,
    Col,
    Container,
    Form,
    FormGroup,
    Input,
    Label,
    Row
} from 'reactstrap';
import AppNavbar from '../navbar/AppNavbar';
import {Link, useNavigate} from "react-router-dom";
import {useAuth} from "../AuthContext";

export default function Login() {
    const [user, setUser] = useState({
        username: "",
        password: ""
    });
    const [badCredentials, setBadCredentials] = useState(false);
    const navigate = useNavigate();
    const {setToken} = useAuth();
    const [monitor, setMonitor] = useState({
        userCount: null,
        orderCount: null
    });

    useEffect(() => {
        setToken(null);
        fetch("/monitor", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(setMonitor);
    }, [setToken]);

    function setUsername(e) {
        setUser({...user, username: e.target.value});
    }

    function setPassword(e) {
        setUser({...user, password: e.target.value});
    }

    function handleSubmit(e) {
        e.preventDefault();
        fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user),
        })
            .then(response => {
                if (response.status === 401)
                    throw new Error(response.statusText);
                return response.json();
            })
            .then(data => {
                setBadCredentials(false);
                setToken(data.token);
                navigate("/orders");
            })
            .catch(e => {
                console.log(e);
                setBadCredentials(true);
            });
    }

    function BadCredentialsMessage({badCredentials}) {
        if (badCredentials)
            return <p style={{color: "red"}}>
                Nombre de usuario o contraseña incorrecto </p>;
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Iniciar sesión</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={user.username} autoComplete={"on"}
                           onChange={setUsername}/>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Constraseña</Label>
                    <Input type="password" id="password" defaultValue={user.password}
                           onChange={setPassword}/>
                </FormGroup>
                <BadCredentialsMessage badCredentials={badCredentials}/>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Enviar</Button>{' '}
                </FormGroup>
            </Form>
            <Link to={"/signup"}>
                Registrarse
            </Link>
        </Container>
        <Container style={{paddingTop: 80}}>
            <Row className="justify-content-md-center">
                <Col className="text-center">
                    <Card style={{width: '18rem', height: "14rem", display: "inline-block"}}>
                        <CardBody>
                            <CardTitle tag="h5">{monitor.userCount}</CardTitle>
                            <CardSubtitle className="mb-2 text-muted" tag="h6">
                                Usuarios activos
                            </CardSubtitle>
                        </CardBody>
                        <img
                            alt="user icon"
                            src={require("./user.png")}
                            width="110"
                        />
                    </Card>
                </Col>
                <Col className="text-center">
                    <Card style={{width: '18rem', height: "14rem", display: "inline-block"}}>
                        <CardBody>
                            <CardTitle tag="h5">{monitor.orderCount}</CardTitle>
                            <CardSubtitle className="mb-2 text-muted" tag="h6">
                                Pedidos creados
                            </CardSubtitle>
                        </CardBody>
                        <img
                            alt="order icon"
                            src={require("./order.png")}
                            width="100"
                            className={"mx-auto"}
                        />
                    </Card>
                </Col>

            </Row>
        </Container>
    </div>
}