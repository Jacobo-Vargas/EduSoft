import { Component } from '@angular/core';

@Component({
  selector: 'app-dropdown-help',
  standalone: false,
  templateUrl: './dropdown-help.component.html',
  styleUrl: './dropdown-help.component.css'
})
export class DropdownHelpComponent {
  isOpen1 = false;
  isOpen2 = false;

  accordions = [
    {
      title: '¿Ofrecen rentabilidad?',
      description: 'Sí, en BELAT trabajamos con inversiones y, como tales, buscan generar una rentabilidad. Y, además del retorno económico, garantizamos el impacto cultural, social y ambiental positivo de tu dinero.\n La pregunta que te invitamos a hacerte, además de cuál será tu rentabilidad, es ¿qué va a suceder en el mundo para que mi rentabilidad exista?. En nuestro caso la respuesta es que una empresa u organización que trabaja en áreas educativas, culturales, sociales y ambientales recibirá un crédito a través de tu inversión.'
    },
    {
      title: '¿Cuál es el rendimiento que ofrecen?',
      description: 'Tenemos más de una forma en la que puedes invertir y que ofrecen distintas condiciones en términos de rentabilidad, diversificación, plazo, etc.; pero que todas las formas tienen algo en común: junto a la rentabilidad estarás también generando impacto positivo en áreas educativas, sociales y ambientales.\n El rendimiento financiero varía según la estructuración crediticia que se realice para la empresa.\n Utilizamos un análisis integrado de impacto positivo y evaluación del riesgo crediticio para definir una tasa de retorno que proporcione sostenibilidad financiera para la empresa y un retorno adecuado al riesgo para el inversor.'
    },
    {
      title: '¿Cuál es la liquidez?',
      description: 'Te invitamos a conocer los distintos tipos de inversión que tenemos para que puedas familiarizarte con las condiciones:\n' +
        '• Financiamiento Colectivo - Contamos con alternativas de inversión donde tú podrás autogestionar tu cartera al elegir cada uno de los créditos en los que quieras participar. De esta forma deberás analizar la tasa de interés, el plazo de duración, la forma de repago y las garantías asociadas a los créditos que decidas participar, ya que las condiciones serán las indicadas para cada caso.\n' +
        '• Fondos de Inversión - Contamos con alternativas de inversión que te permitirán diversificar y tener mayor flexibilidad en los plazos que decidas invertir. Aquí debes consultar las condiciones del fondo específico en el quieras invertir y los plazos de retiro de inversión.'
    },
    {
      title: '¿Qué significa que mi dinero tenga un impacto positivo?',
      description: 'En BELAT trabajamos para garantizar que tu dinero fluya hacia empresas que generen un impacto positivo a través de la estructuración crediticia. El crédito de calidad brinda comodidad financiera para que la empresa sea financieramente sostenible y logre el resultado para el cual se solicitó el dinero. De esta forma tu dinero, además de generar una rentabilidad, está al servicio de la creación y mejora de espacios educativos, el desarrollo de proyectos de vivienda social, la ampliación del abastecimiento a través de energías renovables, la producción de alimentos saludables, etc.'
    },
    {
      title: '¿Qué es la Banca Ética Latinoamericana?',
      description: 'Banca Ética Latinoamericana (BELAT) es un grupo financiero latinoamericano que conecta a inversionistas conscientes con empresas y organizaciones con impacto positivo, permitiendo, además de retornos financieros, apoyo a causas socioambientales. 🌏✨'
    },
    {
      title: '¿Qué hace Banca Ética Latinoamericana?',
      description: 'Impulsamos varios sectores económicos a través de estructuraciones crediticias a medida, en las áreas de Educación y Cultura, Desarrollo Social y Medio Ambiente y . Desde 2016, hemos movido más de USD 135 millones en más de 1.100 operaciones crediticias.'
    },
    {
      title: '¿Cuáles son los criterios para definir si un negocio tiene impacto o no?',
      description: 'La empresa u organización necesita ofrecer, como actividad principal, un producto o servicio que resuelva un problema social o ambiental dentro de las áreas de Educación y Cultura, Desarrollo Social y Medio Ambiente; necesitas estar impulsando la mejora del sector al que perteneces o estar interesado en avanzar de forma comprometida hacia un negocio con impacto positivo en tu sector; y necesita contar y promover buenas prácticas, en temas como relaciones laborales, inclusión, cadena de valor, distribución, entre otros.'
    },
    {
      title: '¿Puedo invertir como empresa/organización?',
      description: 'Sí, puede invertir cualquier tipo de organización, empresa y persona. Nuestros equipos podrán acompañarte para que tu empresa pueda comenzar a invertir.'
    }
  ]

  toggle(section: number) {
    if (section === 1) {
      this.isOpen1 = !this.isOpen1;
    } else if (section === 2) {
      this.isOpen2 = !this.isOpen2;
    }
  }
}
