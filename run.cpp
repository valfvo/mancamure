#include <cstdlib>

#include <fstream>
#include <iostream>
#include <iterator>
#include <regex>
#include <string>

int main(int argc, char** argv)
{
    std::ifstream file("pom.xml");
    std::string data((std::istreambuf_iterator<char>(file)),
                     (std::istreambuf_iterator<char>()));

    std::string args = "";
    for (int i = 1; i < argc; ++i) {
        args += "              <argument>" + std::string(argv[i]) + "</argument>\r\n";
    }
    args += "            ";

    std::string mainClass = "com\\.fvostudio\\.project\\.mancamure\\.App";
    std::string pattern = 
        "( *)<argument>" + mainClass + "</argument>[\\s\\S]*</arguments>";

    std::regex args_regex(pattern);

    std::smatch matches;
    std::regex_search(data, matches, args_regex);
    for (auto match : matches) {
        std::cout << "match:\n";
        std::cout << match.str() << std::endl;
    }

    // std::cout << data << std::endl;
    // std::system("mvn exec:exec");

    return 0;
}
