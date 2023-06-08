package com.algaworks.cobranca.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.TituloFilter;
import com.algaworks.cobranca.repository.TituloRepository;
import com.algaworks.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	@Autowired
	private TituloRepository tituloRepository;

	@Autowired
	private CadastroTituloService cadastroTituloService;

	private static final String CADASTRO_VIEW = "cadastroTitulo";

	// retorna a página cadastroTilo passando o objeto titulo
	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("cadastroTitulo");
		mv.addObject(new Titulo());
		return mv;
	}

	// Salva o objeto titulo e retorna para cadastroTitulo
	@PostMapping
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}

		try {
			cadastroTituloService.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Titulo salvo com sucesso");
			return "redirect:/titulos/novo";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}

	@GetMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
	
		List<Titulo> todosTitulos = cadastroTituloService.filtrar(filtro);

		ModelAndView mv = new ModelAndView("pesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		return mv;
	}

	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}

	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable("codigo") Titulo titulo, RedirectAttributes attributes) {
		cadastroTituloService.excluir(titulo);
		attributes.addFlashAttribute("mensagem", "Titulo apagado com sucesso");
		return "redirect:/titulos";
	}

	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		return cadastroTituloService.receber(codigo);
	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> statusTitulo() {

		return Arrays.asList(StatusTitulo.values());
	}

}
